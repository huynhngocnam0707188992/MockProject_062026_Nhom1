package com.eldercare.modules.careplan_management.careplan_design.service;

import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;

public interface ICareGoalRepository {
    CareGoalEntity saveOne(CareGoalEntity careGoal, CarePlanEntity carePlanEntity);
}
