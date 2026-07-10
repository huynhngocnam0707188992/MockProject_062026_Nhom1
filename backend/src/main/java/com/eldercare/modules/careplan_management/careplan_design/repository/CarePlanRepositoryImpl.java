package com.eldercare.modules.careplan_management.careplan_design.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CarePlanSchema;
import com.eldercare.modules.careplan_management.careplan_design.repository.jpa.JpaCarePlanRepository;
import com.eldercare.modules.careplan_management.careplan_design.service.ICarePlanRepository;

@Repository
public class CarePlanRepositoryImpl implements ICarePlanRepository {
    private final JpaCarePlanRepository jpaCarePlanRepository;

    public CarePlanRepositoryImpl(JpaCarePlanRepository jpaCarePlanRepository) {
        this.jpaCarePlanRepository = jpaCarePlanRepository;
    }

    @Override
    public CarePlanEntity findById(int id) {
        Optional<CarePlanSchema> carePlanSchema = this.jpaCarePlanRepository.findById(Long.valueOf(id));
        return new CarePlanEntity();
    }

}
