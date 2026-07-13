package com.eldercare.modules.careplan_management.careplan_design.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import org.springframework.data.domain.*;
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

import static java.util.stream.Collectors.toList;

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
    @Transactional
    public CarePlanEntity findById(int id) {
        Optional<CarePlanSchema> optionalCarePlanSchema = this.jpaCarePlanRepository.findById(Long.valueOf(id));
        CarePlanSchema carePlanSchema = optionalCarePlanSchema.get();

        List<CareGoalEntity> listCareGoalEntity = carePlanSchema.getListCareGoal().stream().map(goal -> new CareGoalEntity(
                        goal.getId().intValue(),
                        goal.getStatus()))
                .toList();

        List<CareInterventionEntity> listCareInterventionEntity = carePlanSchema.getListCareIntervention().stream().map(intervention -> new CareInterventionEntity(
                        intervention.getId().intValue(),
                        intervention.getAssignedRole()))
                .toList();

        CarePlanEntity carePlanEntity = new CarePlanEntity(
                carePlanSchema.getId().intValue(),
                CarePlanStatusEnum.valueOf(carePlanSchema.getStatus()),
                carePlanSchema.getSignificantChangeFlag(),
                carePlanSchema.getResidentId().intValue(),
                listCareGoalEntity,
                listCareInterventionEntity,
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
                                toList())));

        Map<Long, List<CareInterventionEntity>> interventionMap = jpaCareInterventionRepository
                .findByCarePlanIdIn(carePlanIds)
                .stream()
                .collect(Collectors.groupingBy(
                        intervention -> intervention.getCarePlan().getId(),
                        Collectors.mapping(
                                intervention -> new CareInterventionEntity(
                                        intervention.getId().intValue(),
                                        intervention.getAssignedRole()),
                                toList())));

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

    @Override
    @Transactional
    public Page<CarePlanEntity> getAllPagination(ListCarePlanRequestDTO request) {
        Pageable pageable = PageRequest.of(
                request.page,
                request.size,
                Sort.by(request.sortDir, request.sortBy));

        Specification<CarePlanSchema> spec = Specification.unrestricted();

        if (request.residentId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("residentId"), request.residentId));
        }

        if (request.status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), request.status.name()));
        }

        if (request.significantChangeFlag != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("significantChangeFlag"),
                            request.significantChangeFlag));
        }

        Page<CarePlanSchema> page = jpaCarePlanRepository.findAll(spec, pageable);

        List<CarePlanSchema> carePlans = page.getContent();

        if (carePlans.isEmpty()) {
            return Page.empty(pageable);
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
                                toList())));

        Map<Long, List<CareInterventionEntity>> interventionMap = jpaCareInterventionRepository
                .findByCarePlanIdIn(carePlanIds)
                .stream()
                .collect(Collectors.groupingBy(
                        intervention -> intervention.getCarePlan().getId(),
                        Collectors.mapping(
                                intervention -> new CareInterventionEntity(
                                        intervention.getId().intValue(),
                                        intervention.getAssignedRole()),
                                toList())));

        List<CarePlanEntity> entities = carePlans.stream()
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

        return new PageImpl<>(
                entities,
                pageable,
                page.getTotalElements()
        );
    }

    @Override
    @Transactional()
    public List<CarePlanEntity> search(SearchCarePlanRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.page,
                request.size,
                Sort.by(Sort.Direction.DESC, "updatedAt"));

        Specification<CarePlanSchema> spec = Specification.unrestricted();

        // TODO:
        // Search by resident name / resident id
        // Waiting for Resident module
        if (request.keyword != null && !request.keyword.isBlank()) {
        }

        if (request.status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), request.status.name()));
        }

        if (request.significantChangeFlag != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("significantChangeFlag"),
                            request.significantChangeFlag));
        }

        Page<CarePlanSchema> page = jpaCarePlanRepository.findAll(spec, pageable);

        return page.getContent()
                .stream()
                .map(schema -> new CarePlanEntity(
                        schema.getId().intValue(),
                        CarePlanStatusEnum.valueOf(schema.getStatus()),
                        schema.getSignificantChangeFlag(),
                        schema.getResidentId().intValue(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        schema.getCreatedAt(),
                        schema.getUpdatedAt(),
                        schema.getIsDeleted()))
                .toList();
    }

    @Override
    @Transactional()
    public Page<CarePlanEntity> searchPagination(SearchCarePlanRequestDTO request) {

        Pageable pageable = PageRequest.of(
                request.page,
                request.size,
                Sort.by(Sort.Direction.DESC, "updatedAt"));

        Specification<CarePlanSchema> spec = Specification.unrestricted();

        // TODO:
        // Search by resident name / resident id
        // Waiting for Resident module
        if (request.keyword != null && !request.keyword.isBlank()) {
            // TODO
        }

        if (request.status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), request.status.name()));
        }

        if (request.significantChangeFlag != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("significantChangeFlag"),
                            request.significantChangeFlag));
        }

        Page<CarePlanSchema> page = jpaCarePlanRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            return Page.empty(pageable);
        }

        List<CarePlanEntity> entities = page.getContent()
                .stream()
                .map(schema -> new CarePlanEntity(
                        schema.getId().intValue(),
                        CarePlanStatusEnum.valueOf(schema.getStatus()),
                        schema.getSignificantChangeFlag(),
                        schema.getResidentId().intValue(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        schema.getCreatedAt(),
                        schema.getUpdatedAt(),
                        schema.getIsDeleted()))
                .toList();

        return new PageImpl<>(
                entities,
                pageable,
                page.getTotalElements()
        );
    }
}
