package org.tax.mitra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_gst_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGstMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String gstin;

    private Boolean isPrimary;

    private LocalDateTime createdAt;
}