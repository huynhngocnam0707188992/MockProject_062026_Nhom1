package com.eldercare.modules.careplan_management.careplan_design.service;

import java.util.List;

import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;

public interface ICarePlanRepository {

    CarePlanEntity findById(int id);

    void updateOne(CarePlanEntity carePlanEntity);

    List<CarePlanEntity> getAll(ListCarePlanRequestDTO request);

    List<CarePlanEntity> search(SearchCarePlanRequestDTO request);

}
