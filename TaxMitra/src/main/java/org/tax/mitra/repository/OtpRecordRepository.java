package org.tax.mitra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tax.mitra.entity.OtpRecord;

import java.util.Optional;

public interface OtpRecordRepository extends JpaRepository<OtpRecord,String> {
    OtpRecord findByInput(String input);
    Optional<OtpRecord> findTopByInputAndOtpStatusOrderByCreatedOnDesc(String input, OtpRecord.OtpStatus otpStatus);
}
