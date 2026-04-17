package org.tax.mitra.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Builder
public class GstinRequestModel extends RequestContext {

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    private String phoneNumber;

    private String gstin;
}