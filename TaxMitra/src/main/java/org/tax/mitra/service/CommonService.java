package org.tax.mitra.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tax.mitra.common.ResponseContext;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.RequestContext;
import org.tax.mitra.model.ServiceResult;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.tax.mitra.constants.CodeConstants.*;

public abstract class CommonService<T extends RequestContext> {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    ResponseContext context;

    protected abstract Map<String, Object> executeService(Map<String, Object> request) throws InvocationTargetException;

    public abstract ServiceType getServiceType();

    public void execute(T requestModel) {
        try {
            Map<String, Object> request = mapper.convertValue(
                    requestModel, new TypeReference<Map<String, Object>>() {}
            );
            request.put("serviceId", generateServiceId());
            ServiceResult result = mapToServiceResult(executeService(request));
            mapFinalResponse(result);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error executing service: " + getServiceType(), e);
        }
    }

    private void mapFinalResponse(ServiceResult result) {
        if (result.isSuccess()) {
            context.success(SUCCESS, result.getMessage(), result.getData());
        } else {
            context.failure(FAILURE, result.getMessage(), result.getData(), 400);
        }
    }
    private String generateServiceId() {
        return UUID.randomUUID().toString();
    }

    private ServiceResult mapToServiceResult(Map<String, Object> response) {
        ServiceResult result = new ServiceResult();

        Object error = response.get(ERROR);

        boolean isSuccess = (error == null || error.toString().trim().isEmpty());

        result.setSuccess(isSuccess);
        result.setMessage((String) response.get(MESSAGE));
        result.setData(response.get(DATA));

        return result;
    }
}