package org.tax.mitra.model;

import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
public class Otp {

    private String otp;
    private Instant expiryTime;
    private OriginalPayload payload;

    @Data
    public static class OriginalPayload {
        private String serviceId;
        private String phoneNumber;
        private String language;
    }
    public Map<String, Object> toMap() {
        return Map.of(
                "otp", otp,
                "expiryTime", expiryTime,
                "payload", Map.of(
                        "serviceId", payload.getServiceId(),
                        "phoneNumber", payload.getPhoneNumber(),
                        "language",payload.getLanguage()
                )
        );
    }
}