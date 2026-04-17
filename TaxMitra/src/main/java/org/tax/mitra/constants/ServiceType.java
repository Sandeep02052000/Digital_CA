package org.tax.mitra.constants;

public enum ServiceType {

    OTP_GENERATE("GENERATE_OTP"),
    OTP_VALIDATE("VALIDATE_OTP"),
    SESSION_VALIDATE("SESSION_VALIDATE"),
    GSTIN_FETCH("GSTIN_FETCH");
    private final String code;
    ServiceType(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public static ServiceType fromCode(String code) {
        for (ServiceType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid service type: " + code);
    }
}