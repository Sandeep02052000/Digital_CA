package org.tax.mitra.model;

public class RequestContext extends AbstractRequestContext {
    private String serviceId;
    private String language;

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getLanguage() {
        return (language == null || language.isBlank()) ? "en" : language;
    }

    public String getServiceId() {
        return serviceId;
    }
}