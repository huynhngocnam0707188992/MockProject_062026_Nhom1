package com.eldercare.modules.careplan_management.careplan_design.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eldercare.modules.careplan_management.careplan_design.dto.activeCarePlanDTO.ActiveCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.activeCarePlanDTO.ActiveCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.discontinueCarePlanDTO.DiscontinueCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.discontinueCarePlanDTO.DiscontinueCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.CarePlanOutput;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.markSignificantChangeDTO.MarkSignificantChangeRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.markSignificantChangeDTO.MarkSignificantChangeResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CarePlanEntity;

@Service
public class CarePlanServiceImpl implements ICarePlanService {

    private final ICarePlanRepository carePlanRepository;

    public CarePlanServiceImpl(ICarePlanRepository carePlanRepository) {
        this.carePlanRepository = carePlanRepository;
    }

    @Override
    public ActiveCarePlanResponseDTO activateCarePlan(ActiveCarePlanRequestDTO requestDTO) {
        int id = requestDTO.carePlanId;
        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(id);
        carePlanEntity.active();
        this.carePlanRepository.updateOne(carePlanEntity);
        return new ActiveCarePlanResponseDTO(carePlanEntity.getId(), carePlanEntity.getStatus().toString(),
                carePlanEntity.getUpdatedAt().toString());
    }

    @Override
    public DiscontinueCarePlanResponseDTO discontinueCarePlan(DiscontinueCarePlanRequestDTO requestDTO) {
        int id = requestDTO.carePlanId;
        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(id);
        carePlanEntity.discontinue();
        this.carePlanRepository.updateOne(carePlanEntity);
        return new DiscontinueCarePlanResponseDTO(
                carePlanEntity.getId(), carePlanEntity.getStatus().toString(),
                carePlanEntity.getUpdatedAt().toString());

    }

    @Override
    public MarkSignificantChangeResponseDTO markSignificantChange(MarkSignificantChangeRequestDTO requestDTO) {
        int id = requestDTO.carePlanId;
        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(id);
        carePlanEntity.markSignificant();
        this.carePlanRepository.updateOne(carePlanEntity);
        return new MarkSignificantChangeResponseDTO(carePlanEntity.getId(), carePlanEntity.getSignificantFlag(),
                carePlanEntity.getUpdatedAt().toString());

    }

    @Override
    public ListCarePlanResponseDTO listCarePlans(ListCarePlanRequestDTO requestDTO) {

        List<CarePlanEntity> listCarePlanEntity = carePlanRepository.getAll(requestDTO);

        List<CarePlanOutput> listCarePlanOutputs = listCarePlanEntity.stream()
                .map(entity -> {
                    CarePlanOutput output = new CarePlanOutput();

                    output.id = entity.getId();
                    output.status = entity.getStatus().name();
                    output.significantFlag = entity.getSignificantFlag();
                    output.residentId = entity.getResidentId();
                    output.goal = entity.getGoal() == null ? null : entity.getGoal().name();
                    output.assingedRole = entity.getAssingedRole();
                    output.createdAt = entity.getCreatedAt() == null
                            ? null
                            : entity.getCreatedAt().toString();
                    output.updatedAt = entity.getUpdatedAt() == null
                            ? null
                            : entity.getUpdatedAt().toString();
                    output.isDeleted = entity.getIsDeleted();

                    return output;
                })
                .toList();

        // TODO: tạo ListCarePlanResponseDTO rồi return
        return new ListCarePlanResponseDTO(listCarePlanOutputs);
    }

}
