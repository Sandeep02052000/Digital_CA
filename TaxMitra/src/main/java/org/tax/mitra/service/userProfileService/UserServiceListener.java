package org.tax.mitra.service.userProfileService;

import org.tax.mitra.model.LoginUserRequest;
import org.tax.mitra.model.RegisterUserRequest;
import org.tax.mitra.model.UniquenessCheckRequest;

public interface UserServiceListener {
    void loginUser(LoginUserRequest request);
    void registerUser(RegisterUserRequest request);
    void checkIfUnique(UniquenessCheckRequest request);
}
