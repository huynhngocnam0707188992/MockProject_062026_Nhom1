package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByResidentId(Long residentId);
}
