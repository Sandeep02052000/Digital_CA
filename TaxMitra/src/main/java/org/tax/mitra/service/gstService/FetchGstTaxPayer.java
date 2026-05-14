package org.tax.mitra.service.gstService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.Constants;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.contract.ThirdPartyEigCaller;
import org.tax.mitra.entity.User;
import org.tax.mitra.exception.GstException;
import org.tax.mitra.model.GstinRequestModel;
import org.tax.mitra.repository.UserGstinRepository;
import org.tax.mitra.service.CommonService;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.internal.util.config.ConfigurationHelper.extractValue;
import static org.tax.mitra.constants.CodeConstants.GSTIN_REGEX;
import static org.tax.mitra.constants.CodeConstants.PAN_REGEX;

@Service
public class FetchGstTaxPayer extends CommonService<GstinRequestModel> {

    private final SystemPreferenceCache preferenceCache;
    private final UserGstinRepository repository;
    private final TaxConfiguration configuration;
    private final ThirdPartyEigCaller caller;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public FetchGstTaxPayer(SystemPreferenceCache preferenceCache,
                            UserGstinRepository repository,
                            TaxConfiguration configuration,
                            ThirdPartyEigCaller caller) {
        this.preferenceCache = preferenceCache;
        this.repository = repository;
        this.configuration = configuration;
        this.caller = caller;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.GSTIN_FETCH;
    }

    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {
        String input = extractValue("gstin", request, () -> {throw new GstException("400", "GSTIN missing");});
        String msisdn = extractValue("phoneNumber", request, () -> {throw new GstException("400", "Phone Number missing");});
        String type = validate(input);
        return processByType(input, type,msisdn);
    }

    private String extractInput(Map<String, Object> request) {
        Object value = request.get("gstin");

        if (value == null) {
            throw new GstException("400", "GSTIN/PAN input is required");
        }
        return value.toString().trim();
    }

    private String validate(String input) {
        if (input.isEmpty()) {
            throw new GstException("400", "Input cannot be empty");
        }
        if (input.matches(preferenceCache.getValue(PAN_REGEX))) {
            return "PAN";
        }
        if (input.matches(preferenceCache.getValue(GSTIN_REGEX))) {
            return "GSTIN";
        }
        return "INVALID";
    }

    private Map<String, Object> processByType(String input, String type, String msisdn) {
        switch (type) {
            case "PAN":
                return handlePAN(input,msisdn);
            case "GSTIN":
                return handleGSTIN(input,msisdn);
            default:
                throw new GstException("400", "Invalid PAN/GSTIN format");
        }
    }

    private Map<String, Object> handlePAN(String pan, String msisdn) {
        //TODO: Fetch the Gst Tax Payer details through PAN Number
        return Map.of(
                "type", "PAN",
                "value", pan
        );
    }

    private Map<String, Object> handleGSTIN(String gstin, String msisdn) {
        Map<String,Object> responseMap = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        try {
            Optional<User> user = repository.findByMsisdnAndGstin(msisdn, gstin);
            if(user.isPresent())
            {
                responseMap.put("message","Details fetched successfully from Database");
                data = convertToMap(user.get());
            } else {
                String uri = configuration.getPropertyByServiceCode(Constants.THIRD_PARTY_URI.getValue(),ServiceType.GSTIN_FETCH.toString(),null);
                ResponseEntity<Map> response = caller.get(uri,Map.of("gstin",gstin));
                if(response.getStatusCode().is2xxSuccessful()) {
                    Map<String,Object> responseBody = response.getBody();
                    if((boolean)responseBody.get("error")) {
                        responseMap.put("error","third.party.failure");
                    } else {
                        data = (Map<String, Object>) responseBody.get("data");
                    }
                    responseMap.put("message","Details fetched successfully");
                }
            }
            responseMap.put("data",data);
            return responseMap;
        } catch (Exception ex) {
            throw new GstException("400","Request failed, please try again");
        }
    }

    private Map<String, Object> convertToMap(Object data) {
        return mapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
    }
}