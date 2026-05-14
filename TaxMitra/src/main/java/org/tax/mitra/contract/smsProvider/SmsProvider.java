package org.tax.mitra.contract.smsProvider;

public interface SmsProvider {
    void sendSms(String mobile, String message);
}