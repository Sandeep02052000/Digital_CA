package org.tax.mitra.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_gstin",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "gstin"})
        },
        indexes = {
                @Index(name = "idx_gstin", columnList = "gstin"),
                @Index(name = "idx_user", columnList = "user_id")
        }
)
public class UserGstin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "gstin", nullable = false, length = 15)
    private String gstin;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(name = "legal_name", length = 255)
    private String legalName;

    @Column(name = "trade_name", length = 255)
    private String tradeName;

    @Column(name = "constitution_of_business", length = 100)
    private String constitutionOfBusiness;

    @Column(name = "gst_status", length = 50)
    private String gstStatus;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "gst_last_updated")
    private LocalDate gstLastUpdated;

    @Column(name = "address_line", columnDefinition = "TEXT")
    private String addressLine;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "state_code", length = 10)
    private String stateCode;

    @Column(name = "state_name", length = 100)
    private String stateName;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "nature_of_business", length = 255)
    private String natureOfBusiness;

    @Column(name = "is_primary")
    private Boolean primaryFlag = true;

    @Column(name = "is_active")
    private Boolean activeFlag = true;

    @Column(name = "gst_verified_at")
    private LocalDateTime gstVerifiedAt;

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

    public void setGstin(String gstin) {
        this.gstin = gstin;

        if (gstin != null && gstin.length() >= 12) {
            this.panNumber = gstin.substring(2, 12);
        }
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGstin() {
        return gstin;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getConstitutionOfBusiness() {
        return constitutionOfBusiness;
    }

    public void setConstitutionOfBusiness(String constitutionOfBusiness) {
        this.constitutionOfBusiness = constitutionOfBusiness;
    }

    public String getGstStatus() {
        return gstStatus;
    }

    public void setGstStatus(String gstStatus) {
        this.gstStatus = gstStatus;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getGstLastUpdated() {
        return gstLastUpdated;
    }

    public void setGstLastUpdated(LocalDate gstLastUpdated) {
        this.gstLastUpdated = gstLastUpdated;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public Boolean getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Boolean primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public LocalDateTime getGstVerifiedAt() {
        return gstVerifiedAt;
    }

    public void setGstVerifiedAt(LocalDateTime gstVerifiedAt) {
        this.gstVerifiedAt = gstVerifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}