package com.eldercare.modules.careplan_management.careplan_design.service;

import java.util.List;

import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;
import org.springframework.data.domain.Page;

public interface ICarePlanRepository {

    CarePlanEntity findById(int id);

    void updateOne(CarePlanEntity carePlanEntity);

    List<CarePlanEntity> getAll(ListCarePlanRequestDTO request);

    Page<CarePlanEntity> getAllPagination(ListCarePlanRequestDTO request);

    List<CarePlanEntity> search(SearchCarePlanRequestDTO request);

    Page<CarePlanEntity> searchPagination(SearchCarePlanRequestDTO request);

    void saveOne(CarePlanEntity carePlanEntity);

    ResidentEntity getResidentInfo(long id);

}
