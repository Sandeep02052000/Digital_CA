package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tax.mitra.entity.User;

import java.util.Optional;

public interface UserGstinRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long id);

    @Query(value = """
        SELECT ug.*
        FROM user_gstin ug
        JOIN users u ON u.id = ug.user_id
        WHERE u.input = :msisdn
        AND ug.gstin = :gstin
    """, nativeQuery = true)
    Optional<User> findByMsisdnAndGstin(
            @Param("msisdn") String msisdn,
            @Param("gstin") String gstin
    );
}