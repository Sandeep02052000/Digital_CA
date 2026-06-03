package org.tax.mitra.service.userProfileService;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.constants.CodeConstants;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.entity.User;
import org.tax.mitra.exception.GstException;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.model.RegisterUserRequest;
import org.tax.mitra.repository.UserRepository;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.userAuthService.AuthenticationService;
import org.tax.mitra.util.IdGeneratorUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@Slf4j
public class UserCreationService extends CommonService<RegisterUserRequest> {

    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
    private static final String VALID_PAN_TYPES = "PCHABGJLFT";

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SystemPreferenceCache cache;

    @Override
    public ServiceType getServiceType() {
        return ServiceType.REG_USER;
    }

    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {
        log.info("User creation request received :: {}",request);
        String type = getString(request, CodeConstants.TYPE);
        String value = getString(request, CodeConstants.VALUE);
        String name = getString(request, CodeConstants.NAME);
        String pan = getString(request, CodeConstants.PAN_NUMBER);
        String gender = getString(request, CodeConstants.GENDER);
        String authType = getString(request, CodeConstants.AUTH_TYPE);
        String authValue = getString(request, CodeConstants.AUTH_VALUE);
        String confirmAuthValue = getString(request, CodeConstants.CONFIRM_AUTH_VALUE);

        validateType(type);
        validateValue(value);
        validateName(name);
        validateGender(gender);
        pan = validateAndFormatPan(pan);

        authService.validateAuth(authType, authValue, confirmAuthValue);

        assert gender != null;
        User user = createUser(type, value, name, pan, gender,authValue);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("message", "User registered successfully");
        return prepareResponse(user);
    }

    private Map<String, Object> prepareResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("input",user.getInput());
        response.put("data",data);
        response.put("message", "User registered successfully");
        return response;
    }

    private User createUser(String type, String value, String name, String pan, String gender,String authValue) {
        try {
            User user = new User();

            user.setUserId(IdGeneratorUtil.generateUniqueId("US"));
            user.setInput(value);
            user.setName(name);
            user.setPan(pan);
            user.setGender(User.Gender.valueOf(gender.toUpperCase()));
            user.setPassword(passwordEncoder.encode(authValue));
            user.setCountryCode("+91");
            user.setIsVerified(false);
            user.setIsActive(true);

            if (CodeConstants.PHONE.equalsIgnoreCase(type)) {
                validatePhone(value);
                user.setPhone(value);
            } else if (CodeConstants.EMAIL.equalsIgnoreCase(type)) {
                user.setEmail(value);
            }

            return repository.save(user);

        } catch (DataIntegrityViolationException ex) {
            throw new OtpException("User already exists with provided details", ErrorCodes.BAD_REQUEST);
        }
    }

    private void validateType(String type) {
        if (StringUtil.isNullOrEmpty(type)) {
            throw new GstException("Type is required", ErrorCodes.BAD_REQUEST);
        }

        if (!CodeConstants.EMAIL.equalsIgnoreCase(type)
                && !CodeConstants.PHONE.equalsIgnoreCase(type)) {
            throw new GstException("Invalid type. Allowed values are phone/email", ErrorCodes.BAD_REQUEST);
        }
    }

    private void validateValue(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            throw new GstException("Value is required", ErrorCodes.BAD_REQUEST);
        }
    }

    private void validateName(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            throw new GstException("Name is required", ErrorCodes.BAD_REQUEST);
        }
    }

    private void validatePhone(String phone) {
        if (!phone.matches(cache.getValue(CodeConstants.MOBILE_REGEX))) {
            throw new GstException("Invalid phone number", ErrorCodes.BAD_REQUEST);
        }
    }

    private void validateGender(String gender) {
        if (StringUtil.isNullOrEmpty(gender) || !User.contains(gender)) {
            throw new GstException("Invalid gender", ErrorCodes.BAD_REQUEST);
        }
    }

    private String validateAndFormatPan(String pan) {
        if (StringUtil.isNullOrEmpty(pan)) {
            throw new GstException("PAN number is required", ErrorCodes.BAD_REQUEST);
        }

        pan = pan.trim().toUpperCase();

        if (pan.length() != 10) {
            throw new GstException("PAN must be exactly 10 characters", ErrorCodes.BAD_REQUEST);
        }

        if (!PAN_PATTERN.matcher(pan).matches()) {
            throw new GstException("Invalid PAN format. Expected format: AAAAA9999A", ErrorCodes.BAD_REQUEST);
        }

        char panType = pan.charAt(3);
        if (VALID_PAN_TYPES.indexOf(panType) == -1) {
            throw new GstException("Invalid PAN type", ErrorCodes.BAD_REQUEST);
        }

        if (pan.startsWith("AAAAA") || pan.equals("ABCDE1234F")) {
            throw new GstException("Test or dummy PAN numbers are not allowed", ErrorCodes.BAD_REQUEST);
        }

        return pan;
    }

    private String getString(Map<String, Object> request, String key) {
        Object value = request.get(key);
        return value == null ? null : value.toString().trim();
    }
}