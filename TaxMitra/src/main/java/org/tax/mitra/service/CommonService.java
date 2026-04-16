package org.tax.mitra.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tax.mitra.common.ResponseContext;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.RequestContext;

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
        Map<String, Object> request = mapper.convertValue(requestModel, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> response = new HashMap<>();
        try {
            request.put("serviceId", generateServiceId());
            response = executeService(request);
            mapFinalResponse(response);

        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error executing service: " + getServiceType(), e);
        }
    }

    private void mapFinalResponse(Map<String, Object> response) {
        Object error = response.get(ERROR);
        if (error == null || error.toString().trim().isEmpty()) {
            context.setSuccess(true);
            context.setCode(SUCCESS);
            context.setMessage((String) response.get(MESSAGE));
            context.setData(response.get(DATA));
            context.setHttpStatus(200);
        } else {
            context.setSuccess(false);
            context.setCode(FAILURE);
            context.setMessage((String) response.get(MESSAGE));
            context.setData(response.get(DATA));
            context.setHttpStatus(400);
        }
    }

    private String generateServiceId() {
        return UUID.randomUUID().toString();
    }
}