package org.tax.mitra.service.otpService;

import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.*;
import org.tax.mitra.entity.OtpRecord;
import org.tax.mitra.entity.User;
import org.tax.mitra.entity.UserGstin;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.repository.OtpRecordRepository;
import org.tax.mitra.repository.UserGstinRepository;
import org.tax.mitra.repository.UserRepository;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.sessionService.GenerateSession;
import org.tax.mitra.service.userProfileService.UserCreation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.tax.mitra.constants.CodeConstants.*;

@Component
public class OtpValidateService extends CommonService<ValidateOtpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(OtpValidateService.class);
    private static final String OTP = "otp";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String OTP_SESSION_PREFIX = "OTP_SESSION:";
    private final RedisTemplate<String, String> redisTemplate;
    private final TaxConfiguration configuration;
    private final SystemPreferenceCache cache;
    private final OtpRecordRepository repository;
    private final UserRepository userRepository;
    private final UserGstinRepository userGstinRepository;
    private final UserCreation creation;
    private  final DefaultRedisScript<String> otpVerifyScript;
    private final GenerateSession session;

    @Autowired
    public OtpValidateService(RedisTemplate<String, String> redisTemplate,
                              TaxConfiguration configuration,
                              SystemPreferenceCache cache,
                              OtpRecordRepository repository,
                              UserRepository userRepository, UserGstinRepository userGstinRepository, UserCreation creation, DefaultRedisScript<String> otpVerifyScript,
                              GenerateSession session) {
        this.redisTemplate = redisTemplate;
        this.configuration = configuration;
        this.cache = cache;
        this.repository = repository;
        this.userRepository = userRepository;
        this.userGstinRepository = userGstinRepository;
        this.creation = creation;
        this.otpVerifyScript = otpVerifyScript;
        this.session = session;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.OTP_VALIDATE;
    }

    /**
     * @param request
     * @return
     * @throws IllegalAccessError
     * @throws InvocationTargetException
     */
    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {

        String otp = (String) request.get("otp");
        String type = (String) request.get("type");
        String value = (String) request.get("value");

        if (StringUtil.isNullOrEmpty(otp)
                || StringUtil.isNullOrEmpty(type)
                || StringUtil.isNullOrEmpty(value)) {
            throw new OtpException("Invalid request", ErrorCodes.BAD_REQUEST);
        }

        type = type.trim().toLowerCase();
        value = value.trim();

        String input = value;
        if ("phone".equals(type)) {
            input = value;
        } else if ("email".equals(type)) {
            input = value;
        } else {
            throw new OtpException("Invalid type. Allowed: phone/email", ErrorCodes.BAD_REQUEST);
        }
        if (!validateOtp(otp)) {
            throw new OtpException("Invalid OTP", ErrorCodes.BAD_REQUEST);
        }
        boolean isEnabled = Boolean.parseBoolean(
                configuration.getPropertyByServiceCode(
                        Constants.REDIS_CONFIG_ENABLED.getValue(),
                        ServiceConstant.GEN_OTP.getValue(),
                        "True"
                )
        );
        if (isEnabled) {
            String key = REDIS_KEY_PREFIX + input;
            String result = redisTemplate.execute(
                    otpVerifyScript,
                    java.util.Collections.singletonList(key),
                    otp
            );
            switch (result) {
                case "VERIFIED":
                    return executeInternalGstinValidate(input);
                case "INVALID":
                    throw new OtpException("Invalid OTP, please try again", ErrorCodes.BAD_REQUEST);
                case "MAX_ATTEMPTS":
                    throw new OtpException("Maximum attempts reached. OTP expired.", ErrorCodes.BAD_REQUEST);
                case "EXPIRED":
                default:
                    throw new OtpException("OTP expired, please try again", ErrorCodes.BAD_REQUEST);
            }
        } else {
            Optional<OtpRecord> optionalRecord =
                    repository.findTopByInputAndOtpStatusOrderByCreatedOnDesc(
                            input, OtpRecord.OtpStatus.ACTIVE
                    );
            if (optionalRecord.isEmpty()) {
                throw new OtpException("OTP not found or already used", ErrorCodes.BAD_REQUEST);
            }
            OtpRecord record = optionalRecord.get();
            if (record.isExpired()) {
                record.setOtpStatus(OtpRecord.OtpStatus.EXPIRED);
                repository.save(record);
                throw new OtpException("OTP Expired, please try again!", ErrorCodes.BAD_REQUEST);
            }
            if (!otp.equals(record.getOtp())) {
                int attempts = record.getInvalidAttempts() + 1;
                record.setInvalidAttempts(attempts);
                if (attempts >= 3) {
                    record.setOtpStatus(OtpRecord.OtpStatus.INVALID);
                }
                repository.save(record);
                throw new OtpException("Invalid OTP, please try again", ErrorCodes.BAD_REQUEST);
            }
            record.setOtpStatus(OtpRecord.OtpStatus.USED);
            repository.save(record);
            return executeInternalGstinValidate(input);
        }
    }
    private Map<String, Object> executeInternalGstinValidate(String input) {
        User user = userRepository.findByInput(input).
                orElseGet(() -> creation.createUser(input));
        Optional<UserGstin> gst = userGstinRepository.findByUserId(user.getId());
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        response.put(MESSAGE, "OTP Verified Successfully");
        data.put("userId",user.getId());
        data.put("isNewUser",gst.isEmpty());
        data.put("hasGstin",gst.isPresent());
        data.put("nextScreen",gst.isPresent() ? "dashboard":"gstin");
        data.put("sessionId", session.createTemporarySessionForGstInUserValidation(OTP_SESSION_PREFIX, input));
        response.put(DATA, data);
        return response;
    }
    private boolean validateOtp(String otp) {
        int otpLength;
        try {
            otpLength = Integer.parseInt(cache.getValue(OTP_LENGTH));
        } catch (Exception e) {
            otpLength = CodeConstants.DEFAULT_OTP_LENGTH;
        }
        return otp.length() == otpLength;
    }
}
