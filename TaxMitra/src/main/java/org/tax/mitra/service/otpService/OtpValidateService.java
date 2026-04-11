package org.tax.mitra.service.otpService;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.Constants;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.constants.ServiceConstant;
import org.tax.mitra.entity.OtpRecord;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.repository.OtpRecordRepository;
import org.tax.mitra.service.CommonService;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.tax.mitra.constants.CodeConstants.*;


public class OtpValidateService extends CommonService<ValidateOtpRequest> {
    private static final String OTP = "otp";
    private static final String PHONE_NUMBER = "phoneNumber";
    private final RedisTemplate<String, String> redisTemplate;
    private final TaxConfiguration configuration;
    private final SystemPreferenceCache cache;
    private final OtpRecordRepository repository;

    @Autowired
    public OtpValidateService(RedisTemplate<String, String> redisTemplate,
                              TaxConfiguration configuration,
                              SystemPreferenceCache cache,
                              OtpRecordRepository repository) {
        this.redisTemplate = redisTemplate;
        this.configuration = configuration;
        this.cache = cache;
        this.repository = repository;
    }

    /**
     * @param request
     * @return
     * @throws IllegalAccessError
     * @throws InvocationTargetException
     */
    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) throws IllegalAccessError, InvocationTargetException {
        String otp = (String) request.get(OTP);
        String msisdn = (String) request.get(PHONE_NUMBER);
        if(StringUtil.isNullOrEmpty(otp) || StringUtil.isNullOrEmpty(msisdn)) {
            throw new OtpException("Invalid request payload", ErrorCodes.BAD_REQUEST);
        }
        if(!validateMsisdn(msisdn)) {
            throw new OtpException("Invalid Phone Number",ErrorCodes.BAD_REQUEST);
        }
        if(!validateOtp(otp)) {
            throw new OtpException("Otp is not valid",ErrorCodes.BAD_REQUEST);
        }
        boolean isEnabled = Boolean.parseBoolean(configuration.getPropertyByServiceCode(Constants.REDIS_CONFIG_ENABLED.getValue(), ServiceConstant.GEN_OTP.getValue(), "True"));
        if(isEnabled) {
            String otpFromCache = redisTemplate.opsForValue().get(msisdn);
            if(StringUtil.isNullOrEmpty(otpFromCache)) {
                throw new OtpException("OTP Expired, please try again!",ErrorCodes.BAD_REQUEST);
            } else if(!otp.equalsIgnoreCase(otpFromCache)) {
                throw new OtpException("Invalid OTP, please try with the valid OTP",ErrorCodes.BAD_REQUEST);
            } else {
                return Map.of(STATUS,OK_STATUS);
            }
        } else {
            Optional<OtpRecord> optionalRecord =
                    repository.findTopByPhoneNumberAndOtpStatusOrderByCreatedOnDesc(
                            msisdn, OtpRecord.OtpStatus.ACTIVE);
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
            return Map.of(STATUS,OK_STATUS);
        }
    }
    private boolean validateOtp(String otp) {
        if(otp.length() == Integer.parseInt(cache.getValue(OTP_LENGTH))) {
            return true;
        }
        return false;
    }
    private boolean validateMsisdn(String msisdn) {
        if (msisdn == null || msisdn.isEmpty()) {
            return false;
        }
        String regex = cache.getValue(MOBILE_REGEX);
        return msisdn.matches(regex);
    }
}
