package com.eldercare.modules.facility.repository;

import com.eldercare.modules.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityCode(String facilityCode);
    
    Page<Facility> findByFacilityCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrLicenseNumberContainingIgnoreCase(
            String code, String name, String license, Pageable pageable);
}