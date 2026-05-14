package org.tax.mitra.contract.smsProvider;

import org.springframework.stereotype.Component;
import org.tax.mitra.contract.smsProvider.SmsProvider;

@Component("twilioSmsProvider")
public class TwilioSmsProvider implements SmsProvider {

    @Override
    public void sendSms(String mobile, String message) {
        // TODO: Twilio integration
    }
}