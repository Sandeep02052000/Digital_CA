package org.tax.mitra.service.userProfileService;

import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.UniquenessCheckRequest;
import org.tax.mitra.service.CommonService;

import java.util.Map;

public class UniquenessCheckService extends CommonService<UniquenessCheckRequest> {
    @Override
    public ServiceType getServiceType() {
        return ServiceType.CHECK_IF_UNIQUE;
    }
    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {

        return null;
    }
}
