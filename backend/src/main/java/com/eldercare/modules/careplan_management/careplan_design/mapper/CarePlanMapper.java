package com.eldercare.modules.careplan_management.careplan_design.mapper;

import com.eldercare.common.enums.CarePlanStatusEnum;
import com.eldercare.modules.admin.facility_setup.facility.facility_layout.entity.BedEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareInterventionEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.resident_info.CarePlanResidentInfoEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CarePlanSchema;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;

public class CarePlanMapper {
    public static CarePlanEntity toEntity(CarePlanSchema schema) {
        if (schema == null) return null;

        CarePlanEntity newEntity = new CarePlanEntity();
        newEntity.setId(schema.getId().intValue());
        newEntity.setStatus(CarePlanStatusEnum.valueOf(schema.getStatus()));
        BedEntity bed = schema.getResident().getBed();
        newEntity.setResident(new CarePlanResidentInfoEntity(
                schema.getResident().getId().intValue(),
                schema.getResident().getLastName(),
                schema.getResident().getDateOfBirth(),
                bed != null && bed.getRoom() != null
                        ? bed.getRoom().getRoomNumber()
                        : null,
                bed != null
                        ? bed.getBedNumber()
                        : null,
                schema.getSignificantChangeFlag()
        ));
        newEntity.setListCareGoal(
                schema.getListCareGoal().stream().map(careGoalschema ->
                        new CareGoalEntity(
                                careGoalschema.getId().intValue(),
                                careGoalschema.getTitle(),
                                careGoalschema.getDescription(),
                                careGoalschema.getStatus(),
                                careGoalschema.getListCareIntervention().stream().map(
                                        careInterventionSchema -> new CareInterventionEntity(
                                                careInterventionSchema.getId().intValue(),
                                                careInterventionSchema.getTitle(),
                                                careInterventionSchema.getAssignedRole()
                                        )
                                ).toList()
                        )
                ).toList()
        );
        newEntity.setCreatedAt(schema.getCreatedAt());
        newEntity.setUpdatedAt(schema.getUpdatedAt());
        newEntity.setIsDeleted(schema.getIsDeleted());
        newEntity.setSignificantFlag(schema.getSignificantChangeFlag());
        newEntity.setLastReviewDateTime(schema.getLastReviewedDateTime() == null ? null: schema.getLastReviewedDateTime());
        newEntity.setLastReviewBy(schema.getLastReviewdBy());

        return newEntity;
    }

    public static CarePlanSchema toSchema(CarePlanEntity entity) {
        if (entity == null) return null;

        CarePlanSchema schema = new CarePlanSchema();

        schema.setId((long) entity.getId());
        schema.setStatus(entity.getStatus().name());
        schema.setSignificantChangeFlag(entity.getSignificantFlag());
        schema.setIsDeleted(entity.getIsDeleted());
        schema.setCreatedAt(entity.getCreatedAt());
        schema.setUpdatedAt(entity.getUpdatedAt());
        return schema;
    }
}
