package com.eldercare.modules.careplan_management.careplan_design.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareInterventionSchema;

public interface JpaCareInterventionRepository extends JpaRepository<CareInterventionSchema, Long> {
    List<CareInterventionSchema> findByCarePlanIdIn(List<Long> carePlanIds);

}
