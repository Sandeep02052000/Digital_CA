package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tax.mitra.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhoneNumber(String msisdn);
}
