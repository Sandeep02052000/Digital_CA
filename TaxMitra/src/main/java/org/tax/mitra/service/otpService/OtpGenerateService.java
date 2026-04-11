package org.tax.mitra.service.otpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.Constants;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.constants.ServiceConstant;
import org.tax.mitra.entity.OtpRecord;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.model.Otp;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.repository.OtpRecordRepository;
import org.tax.mitra.service.CommonService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.tax.mitra.constants.CodeConstants.OTP_LENGTH;

@Component
@Slf4j
public class OtpGenerateService extends CommonService<TriggerOtpRequestModel> {
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String REDIS_KEY_PREFIX = "OTP:";
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
    public OtpGenerateService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {
        String phoneNumber = (String) request.get(PHONE_NUMBER);
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new OtpException("Phone number is required", ErrorCodes.BAD_REQUEST);
        }
        phoneNumber = phoneNumber.trim();
        Otp otp = new Otp();
        otp.setPayload(mapper.convertValue(request, Otp.OriginalPayload.class));
        generateOtp(otp, phoneNumber);
        return otp.toMap();
    }
    private void generateOtp(Otp otp, String phoneNumber) {
        String generatedOtp = randomOtpGenerator();
        otp.setOtp(generatedOtp);
        long expirySeconds = getExpirySeconds();
        Instant expiryTime = Instant.now().plusSeconds(expirySeconds);
        otp.setExpiryTime(expiryTime);
        boolean isRedisEnabled = isRedisEnabled();
        log.info("Generating OTP for msisdn={}, redisEnabled={}", phoneNumber, isRedisEnabled);
        if (isRedisEnabled) {
            handleRedisStorage(phoneNumber, generatedOtp, expirySeconds);
        } else {
            handleDatabaseStorage(phoneNumber, generatedOtp);
        }
    }

    private void handleRedisStorage(String phoneNumber, String otp, long expirySeconds) {
        String key = REDIS_KEY_PREFIX + phoneNumber;

        String existingOtp = redisTemplate.opsForValue().get(key);
        if (existingOtp != null) {
            throw new OtpException(
                    "OTP already sent. Please wait before retrying.",
                    ErrorCodes.TOO_MANY_REQUESTS
            );
        }
        redisTemplate.opsForValue().set(
                key,
                otp,
                expirySeconds,
                TimeUnit.SECONDS
        );
    }

    private void handleDatabaseStorage(String phoneNumber, String otp) {
        Optional<OtpRecord> existingRecord =
                repository.findTopByPhoneNumberAndOtpStatusOrderByCreatedOnDesc(
                        phoneNumber,
                        OtpRecord.OtpStatus.ACTIVE
                );
        existingRecord.ifPresent(record -> {
            record.setOtpStatus(OtpRecord.OtpStatus.EXPIRED);
            repository.save(record);
        });
        OtpRecord newRecord = new OtpRecord();
        newRecord.setPhoneNumber(phoneNumber);
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
            log.warn("Invalid OTP_LENGTH config, defaulting to 6");
            otpLength = 6;
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
            log.warn("Invalid OTP expiry config, defaulting to 120 seconds");
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
            log.warn("Redis config missing, defaulting to enabled");
            return true;
        }
    }
}