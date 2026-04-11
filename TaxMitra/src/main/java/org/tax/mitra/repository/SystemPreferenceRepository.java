package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tax.mitra.entity.SystemPreference;

public interface SystemPreferenceRepository extends JpaRepository<SystemPreference,String> {
}
