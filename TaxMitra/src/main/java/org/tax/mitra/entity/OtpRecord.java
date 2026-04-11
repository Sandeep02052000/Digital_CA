package org.tax.mitra.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.tax.mitra.constants.Constants;

import java.time.LocalDateTime;

@Entity
@Table(name = "OTP_RECORD",schema = "tax_maind")
@Data
@NoArgsConstructor
@ToString
public class OtpRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Long otpId;

    @Column(name = "msisdn")
    private String phoneNumber;

    @Column(name = "otp")
    private String otp;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "invalid_attempts", nullable = false)
    private Integer invalidAttempts = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_status", nullable = false, length = 20)
    private OtpStatus otpStatus;

    public enum OtpStatus {
        ACTIVE,
        USED,
        EXPIRED,
        INVALID
    }

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        this.expiresAt = createdOn.plusMinutes(Long.parseLong(Constants.OTP_EXPIRY_IN_MINUTE.getValue()));
    }
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
