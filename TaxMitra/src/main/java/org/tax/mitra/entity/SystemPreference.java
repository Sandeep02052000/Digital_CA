package org.tax.mitra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SYSTEM_PREFERENCE")
@Data
@NoArgsConstructor
@ToString
public class SystemPreference {
    @Id
    @Column(name = "PREFERENCE_CODE")
    String preferenceCode;

    @Column(name = "PREFERENCE_VALUE")
    String preferenceValue;

    @Column(name = "STATUS")
    String status;

}