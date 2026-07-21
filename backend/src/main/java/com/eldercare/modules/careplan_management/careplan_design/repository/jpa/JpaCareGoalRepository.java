package com.eldercare.modules.careplan_management.careplan_design.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareGoalSchema;

public interface JpaCareGoalRepository extends JpaRepository<CareGoalSchema, Long> {
    List<CareGoalSchema> findByCarePlanIdIn(List<Long> carePlanIds);
}
