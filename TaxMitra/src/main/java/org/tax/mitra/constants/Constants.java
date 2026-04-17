package org.tax.mitra.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum Constants {
    REDIS_CONFIG_ENABLED("redis.config.enabled"),
    OTP_EXPIRY_IN_MINUTE("otp.expiry.in.minutes"),
    OTP_EXPIRY_IN_SECOND("otp.expiry.in.second"),
    SESSION_EXPIRY_IN_SECOND("session.expiry.in.second"),
    SPRING_KAFKA_BOOTSTRAP_SERVER("spring.kafka.bootstrap-servers"),
    THIRD_PARTY_URI("third.party.url");
    private final String value;
    Constants(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}