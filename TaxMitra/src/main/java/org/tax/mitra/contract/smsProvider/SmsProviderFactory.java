package org.tax.mitra.contract.smsProvider;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsProviderFactory {

    private final Map<String, SmsProvider> providers;

    public SmsProviderFactory(Map<String, SmsProvider> providers) {
        this.providers = providers;
    }

    public SmsProvider getProvider(String name) {
        return providers.get(name);
    }
}