package org.tax.mitra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TaxConfiguration {
    private Properties properties = new Properties();

    @Autowired
    private Environment environment;

    public String getProperty(String name) {
        if (environment == null) {
            return properties.getProperty(name);
        } else {
            return properties.getProperty(name, environment.getProperty(name));
        }
    }


    public String getProperty(String name, String defaultValue) {
        return environment.getProperty(name, defaultValue);
    }
    public String getPropertyByServiceCode(String name, String serviceCode, String defaultValue) {
        StringBuilder builder = new StringBuilder(name);
        return environment.getProperty(builder.append(".").append(serviceCode).toString(), defaultValue);
    }
    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public boolean getBooleanProperty(String name) {
        return Boolean.parseBoolean(getProperty(name));
    }

}
