package org.tax.mitra.model;

public class RegisterUserRequest extends RequestContext {

    private String type;
    private String value;

    private String authenticationType;
    private String authenticationValue;
    private String confirmedAuthenticationValue;

    private String panNumber;
    private String gender;
    private String name;
    private String dateOfBirth;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getAuthenticationValue() {
        return authenticationValue;
    }

    public void setAuthenticationValue(String authenticationValue) {
        this.authenticationValue = authenticationValue;
    }

    public String getConfirmedAuthenticationValue() {
        return confirmedAuthenticationValue;
    }

    public void setConfirmedAuthenticationValue(String confirmedAuthenticationValue) {
        this.confirmedAuthenticationValue = confirmedAuthenticationValue;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}