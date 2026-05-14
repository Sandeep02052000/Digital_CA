package org.tax.mitra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gst_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GstProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String gstin;

    private String legalName;

    private String tradeName;

    private String status;

    private String registrationDate;

    private String constitution;

    private String taxpayerType;

    @Column(columnDefinition = "TEXT")
    private String businessNatureJson;

    @Column(columnDefinition = "TEXT")
    private String addressJson;

    @Column(columnDefinition = "TEXT")
    private String rawResponse; // full API response

    private LocalDateTime lastFetchedAt;

    private LocalDateTime expiryAt;

    private String source; // GOV_API
}