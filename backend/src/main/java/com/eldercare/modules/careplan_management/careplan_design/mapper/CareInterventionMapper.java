package com.eldercare.modules.careplan_management.careplan_design.mapper;

import com.eldercare.modules.careplan_management.careplan_design.entity.CareInterventionEntity;
import com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareInterventionSchema;

public class CareInterventionMapper {

    public static CareInterventionEntity toEntity(CareInterventionSchema schema) {

        return new CareInterventionEntity(

                schema.getId().intValue(),

                schema.getTitle(),

                schema.getAssignedRole()

        );
    }
}
