package com.eldercare.modules.careplan_management.careplan_design.mapper;

import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareGoalSchema;

public class CareGoalMapper {
    public static CareGoalEntity toEntity(CareGoalSchema schema) {

        return new CareGoalEntity(

                schema.getId().intValue(),

                schema.getTitle(),

                schema.getDescription(),

                schema.getStatus(),

                schema.getListCareIntervention()
                        .stream()
                        .map(CareInterventionMapper::toEntity)
                        .toList()

        );
    }

}
