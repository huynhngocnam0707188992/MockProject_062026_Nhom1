package com.eldercare.modules.careplan_management.careplan_design.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eldercare.modules.careplan_management.careplan_design.dto.addCareGoalDTO.AddCareGoalRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.addCareGoalDTO.AddCareGoalResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;

import jakarta.transaction.Transactional;

@Service
public class CareGoalServiceImpl implements ICareGoalService {

    private final ICarePlanRepository carePlanRepositoy;
    private final ICareGoalRepository careGoalRepository;

    public CareGoalServiceImpl(ICarePlanRepository carePlanRepositoy, ICareGoalRepository careGoalRepository) {
        this.carePlanRepositoy = carePlanRepositoy;
        this.careGoalRepository = careGoalRepository;
    }

    @Override
    @Transactional()
    public AddCareGoalResponseDTO addCareGoal(AddCareGoalRequestDTO requestDTO, int carePlanId) {
        // find care plan
        CarePlanEntity carePlanEntity = this.carePlanRepositoy.findById(carePlanId);

        CareGoalEntity careGoalEntity = new CareGoalEntity();
        // careGoalEntity.setListCareIntervention();
        careGoalEntity.setDescription(requestDTO.careGoalDescription);
        careGoalEntity.setName(requestDTO.careGoalName);
        careGoalEntity.initState();// set it to in progress

        CareGoalEntity careGoalEntityAfterInserted = this.careGoalRepository.saveOne(careGoalEntity,
                carePlanEntity);

        AddCareGoalResponseDTO resposneDto = new AddCareGoalResponseDTO(carePlanEntity.getId(),
                careGoalEntityAfterInserted.getId());
        return resposneDto;
    }

}
