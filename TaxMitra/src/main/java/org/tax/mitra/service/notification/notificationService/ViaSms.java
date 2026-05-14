package org.tax.mitra.service.notification.notificationService;

import org.springframework.stereotype.Service;
import org.tax.mitra.cache.SystemPreferenceCache;
import org.tax.mitra.contract.smsProvider.SmsProvider;
import org.tax.mitra.contract.smsProvider.SmsProviderFactory;
import org.tax.mitra.model.Notification;

@Service("smsService")
public class ViaSms extends NotificationService {

    private final SmsProviderFactory factory;
    private final SystemPreferenceCache preferenceCache;

    public ViaSms(SmsProviderFactory factory, SystemPreferenceCache preferenceCache) {
        this.factory = factory;
        this.preferenceCache = preferenceCache;
    }

    @Override
    public void sendNotification(Notification request) {

        String providerName = preferenceCache.getValue("SMS_PROVIDER");
        SmsProvider provider = factory.getProvider(providerName);

        provider.sendSms(request.getMsisdn(), request.getMessage());
    }

}