package org.tax.mitra.service.otpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.*;
import org.tax.mitra.entity.OtpRecord;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.model.Notification;
import org.tax.mitra.model.Otp;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.repository.OtpRecordRepository;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.notificationService.NotificationService;
import org.w3c.dom.Notation;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.tax.mitra.constants.CodeConstants.*;

@Component
public class OtpGenerateService extends CommonService<TriggerOtpRequestModel> {
    private static final Logger logger = LoggerFactory.getLogger(OtpGenerateService.class);
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TaxConfiguration configuration;
    @Autowired
    private OtpRecordRepository repository;
    @Autowired
    private SystemPreferenceCache preferenceCache;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    public OtpGenerateService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.OTP_GENERATE;
    }
    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {
        logger.info("Inside generate Otp Service :: {}", request);
        Map<String, Object> response = new HashMap<>();
        String type = (String) request.get("type");
        String value = (String) request.get("value");
        if (type == null || type.trim().isEmpty()) {
            throw new OtpException("Type is required (phone/email)", ErrorCodes.BAD_REQUEST);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new OtpException("Value is required", ErrorCodes.BAD_REQUEST);
        }
        type = type.trim().toLowerCase();
        value = value.trim();
        String input = null;
        if ("phone".equals(type)) {
            input = value;
        } else if ("email".equals(type)) {
            input = value;
        } else {
            throw new OtpException("Invalid type. Allowed: phone/email", ErrorCodes.BAD_REQUEST);
        }
        Otp otp = new Otp();
        otp.setPayload(mapper.convertValue(request, Otp.OriginalPayload.class));
        if (generateOtp(otp, input)) {
            response.put(DATA, otp.toMap());
            response.put(MESSAGE, "OTP Generated Successfully");
        } else {
            response.put(MESSAGE, "Failed to generate OTP");
        }
        return response;
    }
    /**
     * @return
     */
    private boolean generateOtp(Otp otp, String input) {
        String generatedOtp = randomOtpGenerator();
        long expirySeconds = getExpirySeconds();
        Instant expiryTime = Instant.now().plusSeconds(expirySeconds);
        otp.setExpiryTime(expiryTime);
        boolean isRedisEnabled = isRedisEnabled();
        logger.info("Generating OTP for input={}, redisEnabled={}", input, isRedisEnabled);
        if (isRedisEnabled) {
            handleRedisStorage(input, generatedOtp, expirySeconds);
        } else {
            handleDatabaseStorage(input, generatedOtp);
        }
        triggerAsyncOtp(input,generatedOtp);
        return true;
    }

    private void triggerAsyncOtp(String input, String otp) {
        if(input.matches(preferenceCache.getValue(MOBILE_REGEX))) {
            //TODO:: Will Implement post MSG91 Integration with the Backend System
        } else {
            notificationService.sendNotification(Notification.builder()
                    .emailNotif(false)
                    .email(input)
                    .message(prepareMessageToSend(otp))
                    .build());
        }
    }

    private String prepareMessageToSend(String otp) {
        return otp;
    }

    private void handleRedisStorage(String input, String otp, long expirySeconds) {
        try {
            String key = REDIS_KEY_PREFIX + input.trim();
            redisTemplate.delete(key);
            redisTemplate.opsForHash().put(key, "code", otp);
            redisTemplate.opsForHash().put(key, "attempts", "0");
            redisTemplate.opsForHash().put(key, "status", "active");
            redisTemplate.expire(key, expirySeconds, TimeUnit.SECONDS);
        } catch (RedisSystemException ex) {
            throw new OtpException(ex.getMessage(), ErrorCodes.BAD_REQUEST);
        }
    }

    private void handleDatabaseStorage(String input, String otp) {
        Optional<OtpRecord> existingRecord =
                repository.findTopByInputAndOtpStatusOrderByCreatedOnDesc(
                        input,
                        OtpRecord.OtpStatus.ACTIVE
                );
        existingRecord.ifPresent(record -> {
            record.setOtpStatus(OtpRecord.OtpStatus.EXPIRED);
            repository.save(record);
        });
        OtpRecord newRecord = new OtpRecord();
        newRecord.setInput(input);
        newRecord.setOtp(otp);
        newRecord.setOtpStatus(OtpRecord.OtpStatus.ACTIVE);
        newRecord.setInvalidAttempts(0);
        repository.save(newRecord);
    }
    private String randomOtpGenerator() {
        int otpLength;
        try {
            otpLength = Integer.parseInt(preferenceCache.getValue(OTP_LENGTH));
        } catch (Exception e) {
            logger.warn("Invalid OTP_LENGTH config, defaulting to 6");
            otpLength = CodeConstants.DEFAULT_OTP_LENGTH;
        }
        if (otpLength <= 0) {
            throw new OtpException("OTP length must be greater than 0", ErrorCodes.BAD_REQUEST);
        }
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(SECURE_RANDOM.nextInt(10));
        }
        return otp.toString();
    }

    private long getExpirySeconds() {
        try {
            return Long.parseLong(
                    configuration.getProperty(
                            Constants.OTP_EXPIRY_IN_SECOND.getValue(),
                            "120"
                    )
            );
        } catch (Exception e) {
            logger.warn("Invalid OTP expiry config, defaulting to 120 seconds");
            return 120L;
        }
    }
    private boolean isRedisEnabled() {
        try {
            return Boolean.parseBoolean(
                    configuration.getPropertyByServiceCode(
                            Constants.REDIS_CONFIG_ENABLED.getValue(),
                            ServiceConstant.GEN_OTP.getValue(),
                            "True"
                    )
            );
        } catch (Exception e) {
            logger.warn("Redis config missing, defaulting to enabled");
            return true;
        }
    }
}