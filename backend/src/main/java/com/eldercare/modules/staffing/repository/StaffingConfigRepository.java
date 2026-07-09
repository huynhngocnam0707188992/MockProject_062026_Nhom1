package com.eldercare.modules.staffing.repository;

import com.eldercare.modules.staffing.entity.StaffingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffingConfigRepository
        extends JpaRepository<StaffingConfig, Long> {
}