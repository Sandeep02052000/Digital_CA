package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tax.mitra.entity.UserGstin;

import java.util.Optional;

public interface UserGstinRepository extends JpaRepository<UserGstin, Long> {

    Optional<UserGstin> findByUserId(Long id);

    @Query(value = """
        SELECT ug.*
        FROM user_gstin ug
        JOIN users u ON u.id = ug.user_id
        WHERE u.msisdn = :msisdn
        AND ug.gstin = :gstin
    """, nativeQuery = true)
    Optional<UserGstin> findByMsisdnAndGstin(
            @Param("msisdn") String msisdn,
            @Param("gstin") String gstin
    );
}