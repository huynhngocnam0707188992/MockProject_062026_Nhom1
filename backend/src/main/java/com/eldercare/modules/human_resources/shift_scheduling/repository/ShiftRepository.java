package com.eldercare.modules.human_resources.shift_scheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.human_resources.shift_scheduling.ShiftEntity;

import java.util.List;

public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {
    List<ShiftEntity> findAllByFacilityIdOrderByStartTimeAsc(Long facilityId);
}
