package org.tax.mitra.model;

import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
public class Otp {

    private Instant expiryTime;
    private OriginalPayload payload;

    @Data
    public static class OriginalPayload {
        private String serviceId;
        private String type;
        private String value;
        private String language;
    }
    public Map<String, Object> toMap() {
        return Map.of(
                "expiryTime", expiryTime,
                "payload", Map.of(
                        "serviceId", payload.getServiceId(),
                        "type", payload.getType(),
                        "value", payload.getValue(),
                        "language",payload.getLanguage()
                )
        );
    }
}