package org.tax.mitra.contract.smsProvider;

import org.springframework.stereotype.Component;

@Component("msg91SmsProvider")
public class Msg91SmsProvider implements SmsProvider {

    @Override
    public void sendSms(String mobile, String message) {
        // TODO: MSG91 integration
    }
}