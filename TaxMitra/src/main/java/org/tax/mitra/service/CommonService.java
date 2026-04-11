package org.tax.mitra.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tax.mitra.model.RequestContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public abstract class CommonService<T extends RequestContext> {

    abstract protected Map<String, Object> executeService(Map<String, Object> request) throws IllegalAccessError, InvocationTargetException;
    public Map<String, Object> execute(Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            request.replace("serviceId", generateServiceId(request));
            response = executeService(request);
        } catch (InvocationTargetException ignored) {

        }
        return response;
    }

    private Object generateServiceId(Map request) {
        return UUID.randomUUID().toString();

    }
}
