package com.eldercare.modules.human_resources.shift_scheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.human_resources.shift_scheduling.StaffingShiftRequirementEntity;

import java.util.List;
import java.util.Optional;

public interface StaffingShiftRequirementRepository
        extends JpaRepository<StaffingShiftRequirementEntity, Long> {

    List<StaffingShiftRequirementEntity> findAllByStaffingConfigId(Long staffingConfigId);

    Optional<StaffingShiftRequirementEntity> findByStaffingConfigIdAndShiftId(
            Long staffingConfigId,
            Long shiftId);
}
