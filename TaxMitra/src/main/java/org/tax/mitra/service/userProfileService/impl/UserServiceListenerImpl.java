package org.tax.mitra.service.userProfileService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.LoginUserRequest;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.ServiceRegistry;
import org.tax.mitra.service.userProfileService.UserServiceListener;

@Service
public class UserServiceListenerImpl implements UserServiceListener {
    private final ServiceRegistry serviceRegistry;

    @Autowired
    public UserServiceListenerImpl(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void loginUser(LoginUserRequest request) {
        CommonService<LoginUserRequest> service =
                (CommonService<LoginUserRequest>) serviceRegistry.getService(ServiceType.USER_LOGIN);
        service.execute(request);
    }
}
