package com.eldercare.modules.careplan_management.careplan_design.service;

import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;

public interface ICarePlanRepository {

    CarePlanEntity findById(int id);

}
