package com.eldercare.modules.careplan_management.careplan_design.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public PagedResponse<ListCarePlanResponseDTO> listCarePlans(ListCarePlanRequestDTO requestDTO) {
//        List<CarePlanEntity> listCarePlanEntity = carePlanRepository.getAll(requestDTO);
        Page<CarePlanEntity> pageCarePlanEntity = carePlanRepository.getAllPagination(requestDTO);
        List<CarePlanEntity> listCarePlanEntity = pageCarePlanEntity.getContent();
        List<CarePlanOutput> listCarePlanOutputs = listCarePlanEntity.stream()
                .map(entity -> {
                    CarePlanOutput output = new CarePlanOutput();

                    output.id = entity.getId();
                    output.status = entity.getStatus().name();
                    output.significantFlag = entity.getSignificantFlag();
                    output.residentId = entity.getResidentId();
                    output.goalCount = entity.getListCareGoal().size();
                    output.interventionCount = entity.getListCareIntervention().size();
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
        return PagedResponse.of(
                new ListCarePlanResponseDTO(listCarePlanOutputs),
                HttpStatus.OK.value(),
                "Success",
                pageCarePlanEntity.getNumber(),
                pageCarePlanEntity.getTotalPages(),
                pageCarePlanEntity.getSize(),
                pageCarePlanEntity.getTotalElements()
        );
    }

    @Override
    public GetCarePlanDetailResponseDTO getCarePlanDetail(GetCarePlanDetailRequestDTO requestDTO) {

        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(requestDTO.id);

        GetCarePlanDetailResponseDTO responseDTO = new GetCarePlanDetailResponseDTO();

        responseDTO.id = carePlanEntity.getId();
        responseDTO.status = carePlanEntity.getStatus().name();
        responseDTO.significantFlag = carePlanEntity.getSignificantFlag();
        responseDTO.residentId = carePlanEntity.getResidentId();
        responseDTO.createdAt = carePlanEntity.getCreatedAt().toString();
        responseDTO.updatedAt = carePlanEntity.getUpdatedAt().toString();
        responseDTO.isDeleted = carePlanEntity.getIsDeleted();

        responseDTO.goals = carePlanEntity.getListCareGoal()
                .stream()
                .map(goal -> {
                    GetCarePlanDetailResponseDTO.Goal dto = new GetCarePlanDetailResponseDTO.Goal();
                    dto.id = goal.getId();
                    dto.status = goal.getStatus().name();
                    return dto;
                })
                .toList();

        responseDTO.interventions = carePlanEntity.getListCareIntervention()
                .stream()
                .map(intervention -> {
                    GetCarePlanDetailResponseDTO.Intervention dto = new GetCarePlanDetailResponseDTO.Intervention();
                    dto.id = intervention.getId();
                    dto.assignedRole = intervention.getAssinedRole();
                    dto.taskCount = 0;
                    return dto;
                })
                .toList();
        return responseDTO;
    }

    @Override
    public PagedResponse<List<SearchCarePlanResponseDTO>> searchCarePlan(
            SearchCarePlanRequestDTO requestDTO) {

        Page<CarePlanEntity> pageCarePlanEntity =
                carePlanRepository.searchPagination(requestDTO);

        List<SearchCarePlanResponseDTO> list = pageCarePlanEntity.getContent()
                .stream()
                .map(entity -> {
                    SearchCarePlanResponseDTO dto = new SearchCarePlanResponseDTO();

                    dto.id = entity.getId();
                    dto.residentId = entity.getResidentId();
                    dto.residentName = "RESIDENT_NAME"; // TODO: lấy từ Resident module
                    dto.status = entity.getStatus().name();
                    dto.significantChangeFlag = entity.getSignificantFlag();
                    dto.createdAt = entity.getCreatedAt() == null
                            ? null
                            : entity.getCreatedAt().toString();
                    dto.updatedAt = entity.getUpdatedAt() == null
                            ? null
                            : entity.getUpdatedAt().toString();

                    return dto;
                })
                .toList();

        return PagedResponse.of(
                list,
                HttpStatus.OK.value(),
                "Success",
                pageCarePlanEntity.getNumber(),
                pageCarePlanEntity.getTotalPages(),
                pageCarePlanEntity.getSize(),
                pageCarePlanEntity.getTotalElements()
        );
    }

}
