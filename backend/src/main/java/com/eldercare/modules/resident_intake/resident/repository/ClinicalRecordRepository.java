package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.ClinicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {
    List<ClinicalRecord> findByResidentIdAndIsDeletedFalse(Long residentId);
    List<ClinicalRecord> findByResidentIdAndRecordTypeAndIsDeletedFalse(Long residentId, String recordType);
}
