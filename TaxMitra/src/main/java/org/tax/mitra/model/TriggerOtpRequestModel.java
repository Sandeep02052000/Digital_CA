package org.tax.mitra.model;

public class TriggerOtpRequestModel extends RequestContext {
    private String phoneNumber;
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}