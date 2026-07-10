package com.eldercare.modules.resident_intake.resident.repository;


import com.eldercare.modules.resident_intake.admission_ledger.AdmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionEntity, Long> {
    List<AdmissionEntity> findByResidentId(Long residentId);
}
