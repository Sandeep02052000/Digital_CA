package org.tax.mitra.service;

import org.springframework.stereotype.Component;
import org.tax.mitra.constants.ServiceType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceRegistry {
    private final Map<ServiceType, CommonService<?>> registry = new EnumMap<>(ServiceType.class);
    public ServiceRegistry(List<CommonService<?>> services) {
        for (CommonService<?> service : services) {
            registry.put(service.getServiceType(), service);
        }
    }
    public CommonService<?> getService(ServiceType type) {
        CommonService<?> service = registry.get(type);
        if (service == null) {
            throw new IllegalStateException("No service registered for: " + type);
        }
        return service;
    }
}