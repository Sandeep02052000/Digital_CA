package org.tax.mitra.model;

public class ValidateOtpRequest extends RequestContext{
    private String phoneNumber;
    private String otp;
    public String getPhoneNumber(){return phoneNumber;}
    public String getOtp(){return otp;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber=phoneNumber;}
    public void setOtp(String otp){this.otp=otp;}
}
