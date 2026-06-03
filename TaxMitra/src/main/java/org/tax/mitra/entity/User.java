package org.tax.mitra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_phone", columnList = "phone"),
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_pan", columnList = "pan")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(unique = true)
    private String input;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "country_code", length = 5)
    private String countryCode = "+91";


    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(unique = true, length = 10)
    private String pan;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "yearly_income")
    private String income;

    @Column(name = "employment_type")
    private EmpType empType;
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
    public enum EmpType {
        SE,
        BO,
        FL,
        ST
    }
    public static boolean contains(String value) {
        return Gender.FEMALE.toString().equalsIgnoreCase(value)
                || Gender.MALE.toString().equalsIgnoreCase(value)
                || Gender.OTHER.toString().equalsIgnoreCase(value);
    }
}