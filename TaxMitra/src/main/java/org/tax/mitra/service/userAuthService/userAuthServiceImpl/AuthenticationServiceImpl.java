package org.tax.mitra.service.userAuthService.userAuthServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.constants.CodeConstants;
import org.tax.mitra.exception.GstException;
import org.tax.mitra.service.userAuthService.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SystemPreferenceCache cache;

    @Autowired
    public AuthenticationServiceImpl(SystemPreferenceCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean validateAuth(String authType, String authValue, String confirmAuthValue) {

        if (!StringUtils.hasText(authType)
                || !StringUtils.hasText(authValue)
                || !StringUtils.hasText(confirmAuthValue)) {
            throw new GstException("Please provide valid authentication details", "invalid.auth");
        }

        if (!CodeConstants.PASSWD_AUTH.equalsIgnoreCase(authType)) {
            throw new GstException("Invalid authentication type", "invalid.auth.type");
        }

        if (!authValue.equals(confirmAuthValue)) {
            throw new GstException("Password and confirm password do not match", "invalid.auth");
        }

        if (!validatePassword(authValue)) {
            throw new GstException("Please provide a valid password", "invalid.password");
        }

        return true;
    }

    private boolean validatePassword(String passwd) {
        int minLength = Integer.parseInt(cache.getValue(CodeConstants.PASSWORD_MIN_LENGTH));
        int maxLength = Integer.parseInt(cache.getValue(CodeConstants.PASSWORD_MAX_LENGTH));

        return passwd.length() >= minLength
                && passwd.length() <= maxLength;
    }
}