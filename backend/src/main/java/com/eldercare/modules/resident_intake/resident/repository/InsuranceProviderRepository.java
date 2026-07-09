package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, Long> {
    Optional<InsuranceProvider> findByProviderName(String providerName);
}
