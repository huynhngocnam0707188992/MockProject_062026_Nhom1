package com.eldercare.modules.careplan_management.careplan_design.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CarePlanSchema;

public interface JpaCarePlanRepository
        extends JpaRepository<CarePlanSchema, Long>, JpaSpecificationExecutor<CarePlanSchema> {

}
