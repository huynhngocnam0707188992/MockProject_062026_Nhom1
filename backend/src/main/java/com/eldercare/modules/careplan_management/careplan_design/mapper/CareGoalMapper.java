package com.eldercare.modules.careplan_management.careplan_design.mapper;

import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareGoalSchema;

public class CareGoalMapper {
    public static CareGoalEntity toEntity(CareGoalSchema schema) {

        return new CareGoalEntity(

                schema.getId() == null ? 0 : schema.getId().intValue(),

                schema.getTitle(),

                schema.getDescription(),

                schema.getStatus(),

                schema.getListCareIntervention()
                        .stream()
                        .map(CareInterventionMapper::toEntity)
                        .toList()

        );
    }

    public static CareGoalSchema toSchema(CareGoalEntity entity) {

        CareGoalSchema schema = new CareGoalSchema();
        if (entity.getId() > 0) {
            schema.setId((long) entity.getId());
        }

        schema.setCarePlan(null);
        schema.setDescription(entity.getDescription());
        schema.setStatus(entity.getStatus());
        // schema.setListCareIntervention(null); handle this later
        schema.setTitle(entity.getName());
        return schema;
    }

}
