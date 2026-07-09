package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.ResidentInsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentInsurancePolicyRepository extends JpaRepository<ResidentInsurancePolicy, Long> {
    List<ResidentInsurancePolicy> findByResidentIdAndIsDeletedFalse(Long residentId);
}
