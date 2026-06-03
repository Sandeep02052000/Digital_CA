package org.tax.mitra.service.userAuthService;

public interface AuthenticationService {
    boolean validateAuth(String authType,String authValue,String confirmAuthValue);
}
