package com.eldercare.modules.careplan_management.careplan_design.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.eldercare.common.enums.CarePlanStatusEnum;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareInterventionEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareGoalSchema;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareInterventionSchema;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CarePlanSchema;
import com.eldercare.modules.careplan_management.careplan_design.repository.jpa.JpaCareGoalRepository;
import com.eldercare.modules.careplan_management.careplan_design.repository.jpa.JpaCareInterventionRepository;
import com.eldercare.modules.careplan_management.careplan_design.repository.jpa.JpaCarePlanRepository;
import com.eldercare.modules.careplan_management.careplan_design.service.ICarePlanRepository;

import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;

@Repository
public class CarePlanRepositoryImpl implements ICarePlanRepository {
        private final JpaCarePlanRepository jpaCarePlanRepository;
        private final JpaCareGoalRepository jpaCareGoalRepository;
        private final JpaCareInterventionRepository jpaCareInterventionRepository;

        public CarePlanRepositoryImpl(JpaCarePlanRepository jpaCarePlanRepository,
                        JpaCareGoalRepository jpaCareGoalRepository,
                        JpaCareInterventionRepository jpaCareInterventionRepository) {
                this.jpaCarePlanRepository = jpaCarePlanRepository;
                this.jpaCareGoalRepository = jpaCareGoalRepository;
                this.jpaCareInterventionRepository = jpaCareInterventionRepository;
        }

        @Override
        public CarePlanEntity findById(int id) {
                Optional<CarePlanSchema> optionalCarePlanSchema = this.jpaCarePlanRepository.findById(Long.valueOf(id));
                CarePlanSchema carePlanSchema = optionalCarePlanSchema.get();
                CarePlanEntity carePlanEntity = new CarePlanEntity(
                                carePlanSchema.getId().intValue(),
                                CarePlanStatusEnum.valueOf(carePlanSchema.getStatus()),
                                carePlanSchema.getSignificantChangeFlag(),
                                carePlanSchema.getResidentId().intValue(),
                                null,
                                null,
                                carePlanSchema.getCreatedAt(),
                                carePlanSchema.getUpdatedAt(),
                                carePlanSchema.getIsDeleted());
                return carePlanEntity;
        }

        @Override
        public void updateOne(CarePlanEntity carePlanEntity) {
                CarePlanSchema schema = new CarePlanSchema(
                                (long) carePlanEntity.getId(),
                                carePlanEntity.getStatus().name(),
                                carePlanEntity.getSignificantFlag(),
                                (long) carePlanEntity.getResidentId(),
                                carePlanEntity.getIsDeleted(),
                                carePlanEntity.getListCareGoal().stream()
                                                .map(careGoalEntity -> {
                                                        CareGoalSchema goalSchema = new CareGoalSchema();
                                                        goalSchema.setId((long) careGoalEntity.getId());
                                                        goalSchema.setStatus(careGoalEntity.getStatus());
                                                        return goalSchema;
                                                })
                                                .toList(),
                                carePlanEntity.getListCareIntervention().stream()
                                                .map(careInterventionEntity -> {
                                                        CareInterventionSchema interventionSchema = new CareInterventionSchema();
                                                        interventionSchema.setId((long) careInterventionEntity.getId());
                                                        interventionSchema.setAssignedRole(
                                                                        careInterventionEntity.getAssinedRole());
                                                        return interventionSchema;
                                                }).toList(),
                                carePlanEntity.getCreatedAt(),
                                carePlanEntity.getUpdatedAt());

                jpaCarePlanRepository.save(schema);
        }

        @Override
        @Transactional
        public List<CarePlanEntity> getAll(ListCarePlanRequestDTO request) {

                Pageable pageable = PageRequest.of(
                                request.page,
                                request.size,
                                Sort.by(request.sortDir, request.sortBy));

                Specification<CarePlanSchema> spec = Specification.unrestricted();

                if (request.residentId != null) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("residentId"), request.residentId));
                }

                if (request.status != null) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), request.status.name()));
                }

                if (request.significantChangeFlag != null) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("significantChangeFlag"),
                                        request.significantChangeFlag));
                }

                Page<CarePlanSchema> page = jpaCarePlanRepository.findAll(spec, pageable);

                List<CarePlanSchema> carePlans = page.getContent();

                if (carePlans.isEmpty()) {
                        return List.of();
                }

                List<Long> carePlanIds = carePlans.stream()
                                .map(CarePlanSchema::getId)
                                .toList();

                Map<Long, List<CareGoalEntity>> goalMap = jpaCareGoalRepository.findByCarePlanIdIn(carePlanIds)
                                .stream()
                                .collect(Collectors.groupingBy(
                                                goal -> goal.getCarePlan().getId(),
                                                Collectors.mapping(
                                                                goal -> new CareGoalEntity(
                                                                                goal.getId().intValue(),
                                                                                goal.getStatus()),
                                                                Collectors.toList())));

                Map<Long, List<CareInterventionEntity>> interventionMap = jpaCareInterventionRepository
                                .findByCarePlanIdIn(carePlanIds)
                                .stream()
                                .collect(Collectors.groupingBy(
                                                intervention -> intervention.getCarePlan().getId(),
                                                Collectors.mapping(
                                                                intervention -> new CareInterventionEntity(
                                                                                intervention.getId().intValue(),
                                                                                intervention.getAssignedRole()),
                                                                Collectors.toList())));

                return carePlans.stream()
                                .map(schema -> new CarePlanEntity(
                                                schema.getId().intValue(),
                                                CarePlanStatusEnum.valueOf(schema.getStatus()),
                                                schema.getSignificantChangeFlag(),
                                                schema.getResidentId().intValue(),
                                                goalMap.getOrDefault(schema.getId(), Collections.emptyList()),
                                                interventionMap.getOrDefault(schema.getId(), Collections.emptyList()),
                                                schema.getCreatedAt(),
                                                schema.getUpdatedAt(),
                                                schema.getIsDeleted()))
                                .toList();
        }
}
