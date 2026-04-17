package org.tax.mitra.service.sessionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tax.mitra.config.TaxConfiguration;
import org.tax.mitra.constants.Constants;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.exception.OtpException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GenerateSession {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSession.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final TaxConfiguration configuration;

    @Autowired
    public GenerateSession(RedisTemplate<String, String> redisTemplate,
                           TaxConfiguration configuration) {
        this.redisTemplate = redisTemplate;
        this.configuration = configuration;
    }

    public String createTemporarySessionForGstInUserValidation(String prefix, String input) {
        String sessionId = UUID.randomUUID().toString();
        String redisKey = prefix + sessionId;
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("input", input);
        sessionData.put("stage", "OTP_VERIFIED");
        sessionData.put("status", "ACTIVE");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(sessionData);
            redisTemplate.opsForValue().set(
                    redisKey,
                    json,
                    java.time.Duration.ofSeconds(getExpirySeconds(prefix))
            );
        } catch (Exception e) {
            logger.error("Error storing OTP session in Redis", e);
            throw new OtpException("Session creation failed", ErrorCodes.BAD_REQUEST);
        }
        return sessionId;
    }

    private long getExpirySeconds(String service) {
        try {
            return Long.parseLong(
                    configuration.getPropertyByServiceCode(
                            Constants.SESSION_EXPIRY_IN_SECOND.getValue(),
                            service,
                            "1200"
                    )
            );
        } catch (Exception e) {
            logger.warn("Invalid SESSION expiry config, defaulting to 1200 seconds");
            return 1200L;
        }
    }
}
