package org.tax.mitra.service.userProfileService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.exception.GstException;
import org.tax.mitra.model.LoginUserRequest;
import org.tax.mitra.service.CommonService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class UserLoginService extends CommonService<LoginUserRequest> {

    private final SystemPreferenceCache cache;

    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String PASSWORD = "password";

    @Autowired
    public UserLoginService(SystemPreferenceCache cache) {
        this.cache = cache;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.USER_LOGIN;
    }

    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {

        log.info("User login request received");

        validateRequest(request);

        String type = request.get(TYPE).toString().trim();
        String value = request.get(VALUE).toString().trim();
        String password = request.get(PASSWORD).toString();

        String loginType = resolveLoginType(type, value);

        // Simulate authentication logic (replace with DB/service call)
        Map<String, Object> response = new HashMap<>();
        response.put("loginType", loginType);
        response.put("status", "SUCCESS");
        response.put("message", "Login validated successfully");

        log.info("User login successful for type: {}", loginType);

        return response;
    }

    private void validateRequest(Map<String, Object> request) {
        if (request == null || request.isEmpty()) {
            throw new GstException("400", "Request cannot be null or empty");
        }
        if (!request.containsKey(TYPE) ||
                !request.containsKey(VALUE) ||
                !request.containsKey(PASSWORD)) {
            throw new GstException("400", "Missing required login fields");
        }
    }

    private String resolveLoginType(String type, String value) {
        if (type == null || value == null) {
            throw new GstException("400", "Invalid login input");
        }
        if ("phone".equalsIgnoreCase(type)) {
            validateMsisdn(value);
            return "MSISDN";
        }
        if ("email".equalsIgnoreCase(type)) {
            validateEmail(value);
            return "EMAIL";
        }
        throw new GstException("400", "Unsupported login type");
    }

    private void validateEmail(String value) {
        String regex = cache.getValue("EMAIL_REGEX");
        if (regex == null || regex.isEmpty()) {
            throw new GstException("500", "Email regex not configured");
        }
        if (!Pattern.compile(regex).matcher(value).matches()) {
            throw new GstException("400", "Invalid email format");
        }
    }

    private void validateMsisdn(String value) {
        String regex = cache.getValue("MSISDN_REGEX");
        if (regex == null || regex.isEmpty()) {
            throw new GstException("500", "MSISDN regex not configured");
        }
        if (!Pattern.compile(regex).matcher(value).matches()) {
            throw new GstException("400", "Invalid phone number format");
        }
    }
}