package com.eldercare.modules.careplan_management.careplan_design.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;
import com.eldercare.modules.careplan_management.careplan_design.mapper.CareGoalMapper;
import com.eldercare.modules.careplan_management.careplan_design.mapper.CarePlanMapper;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareGoalSchema;
import com.eldercare.modules.careplan_management.careplan_design.repository.jpa.JpaCareGoalRepository;
import com.eldercare.modules.careplan_management.careplan_design.service.ICareGoalRepository;

import jakarta.transaction.Transactional;

@Repository
public class CareGoalRepositoryImpl implements ICareGoalRepository {
    private final JpaCareGoalRepository jpaCareGoalRepository;

    public CareGoalRepositoryImpl(JpaCareGoalRepository jpaCareGoalRepository) {
        this.jpaCareGoalRepository = jpaCareGoalRepository;
    }

    @Override
    @Transactional()
    public CareGoalEntity saveOne(CareGoalEntity careGoalEntity, CarePlanEntity carePlanEntity) {
        CareGoalSchema schema = CareGoalMapper.toSchema(careGoalEntity);
        schema.setCarePlan(CarePlanMapper.toSchema(carePlanEntity));
        CareGoalSchema schemaAfterInserted = this.jpaCareGoalRepository.save(schema);
        return CareGoalMapper.toEntity(schemaAfterInserted);
    }

}
