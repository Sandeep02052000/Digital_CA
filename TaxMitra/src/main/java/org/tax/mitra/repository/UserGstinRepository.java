package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tax.mitra.entity.UserGstin;

import java.util.Optional;

public interface UserGstinRepository extends JpaRepository<UserGstin,String> {
    Optional<UserGstin> findByUserId(Long id);
}
