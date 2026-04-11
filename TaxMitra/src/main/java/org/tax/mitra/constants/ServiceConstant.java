package org.tax.mitra.constants;

public enum ServiceConstant {
    GEN_OTP("GEN_OTP");
    private final String value;
    ServiceConstant(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
