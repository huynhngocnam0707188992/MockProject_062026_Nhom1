package com.eldercare.modules.careplan_management.careplan_design.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.eldercare.common.enums.CarePlanStatusEnum;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
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
        Optional<CarePlanSchema> optionalCarePlanSchema = this.jpaCarePlanRepository.findById(Long.valueOf(id));
        CarePlanSchema carePlanSchema = optionalCarePlanSchema.get();
        CarePlanEntity carePlanEntity = new CarePlanEntity(
                carePlanSchema.id.intValue(),
                CarePlanStatusEnum.valueOf(carePlanSchema.status),
                carePlanSchema.significantChangeFlag,
                carePlanSchema.residentId.intValue(),
                null,
                null,
                carePlanSchema.createdAt,
                carePlanSchema.updatedAt,
                carePlanSchema.isDeleted);
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
                carePlanEntity.getCreatedAt(),
                carePlanEntity.getUpdatedAt());

        jpaCarePlanRepository.save(schema);
    }

    @Override
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

        return jpaCarePlanRepository.findAll(spec, pageable)
                .stream()
                .map(schema -> new CarePlanEntity(
                        schema.id.intValue(),
                        CarePlanStatusEnum.valueOf(schema.status),
                        schema.significantChangeFlag,
                        schema.residentId.intValue(),
                        null,
                        null,
                        schema.createdAt,
                        schema.updatedAt,
                        schema.isDeleted))
                .toList();
    }

}
