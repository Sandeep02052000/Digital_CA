package org.tax.mitra.service.userProfileService;

import org.tax.mitra.model.LoginUserRequest;

public interface UserServiceListener {
    void loginUser(LoginUserRequest request);
}
