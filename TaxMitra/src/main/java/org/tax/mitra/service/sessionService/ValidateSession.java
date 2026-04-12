package org.tax.mitra.service.sessionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.exception.SessionException;
import org.tax.mitra.model.ValidateSessionRequest;
import org.tax.mitra.service.CommonService;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateSession extends CommonService<ValidateSessionRequest> {

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String SESSION_ID = "sessionId";
    private static final String SESSION_PREFIX = "OTP_SESSION:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ServiceType getServiceType() {
        return ServiceType.SESSION_VALIDATE;
    }

    @Override
    protected Map<String, Object> executeService(Map<String, Object> request) {
        String msisdn = (String) request.get(PHONE_NUMBER);
        String sessionId = (String) request.get(SESSION_ID);
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Session ID cannot be null or empty");
        }
        String sessionKey = SESSION_PREFIX + sessionId;
        String sessionJson = redisTemplate.opsForValue().get(sessionKey);
        if (sessionJson == null) {
            throw new SessionException("Session expired or invalid", ErrorCodes.BAD_REQUEST);
        }
        Map<String, Object> sessionData;
        try {
            sessionData = objectMapper.readValue(sessionJson, Map.class);
        } catch (Exception e) {
            throw new SessionException("Invalid session data format",ErrorCodes.BAD_REQUEST);
        }
        if (msisdn != null) {
            String storedMobile = (String) sessionData.get("mobile");
            if (storedMobile != null && !storedMobile.equals(msisdn)) {
                throw new SessionException("Session does not belong to user",ErrorCodes.BAD_REQUEST);
            }
        }
        String status = (String) sessionData.get("status");
        if (!"ACTIVE".equals(status)) {
            throw new SessionException("Session is not active",ErrorCodes.BAD_REQUEST);
        }
        redisTemplate.expire(sessionKey, java.time.Duration.ofMinutes(5));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Session is valid");
        response.put("data", sessionData);
        return response;
    }
}