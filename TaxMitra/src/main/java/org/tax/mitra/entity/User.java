package org.tax.mitra.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "input", nullable = false, unique = true, length = 15)
    private String input;
    @Column(name = "country_code", length = 5)
    private String countryCode = "+91";
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "is_phone_verified")
    private Boolean isPhoneVerified = false;
    @Column(name = "onboarding_status", length = 30)
    private String onboardingStatus = "PHONE_VERIFIED";
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public User() {
    }
    public User(String input) {
        this.input = input;
    }
    public Long getId() {
        return id;
    }
    public String getPhoneNumber() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {
        isActive = active;
    }
    public Boolean getIsPhoneVerified() {
        return isPhoneVerified;
    }
    public void setIsPhoneVerified(Boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }
    public String getOnboardingStatus() {
        return onboardingStatus;
    }
    public void setOnboardingStatus(String onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}