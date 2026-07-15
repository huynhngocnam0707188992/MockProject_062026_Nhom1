package com.eldercare.modules.careplan_management.careplan_design.service;

import java.time.OffsetDateTime;
import java.util.List;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.common.enums.CarePlanGoalStatusEnum;
import com.eldercare.common.enums.CarePlanStatusEnum;
import com.eldercare.modules.careplan_management.careplan_design.dto.createCarePlanDTO.CreateCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.createCarePlanDTO.CreateCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareGoalEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.CareInterventionEntity;
import com.eldercare.modules.careplan_management.careplan_design.entity.resident_info.CarePlanResidentInfoEntity;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CarePlanServiceImpl.class);
    private final ICarePlanRepository carePlanRepository;

    public CarePlanServiceImpl(ICarePlanRepository carePlanRepository) {
        this.carePlanRepository = carePlanRepository;
    }

    @Override
    public ActiveCarePlanResponseDTO activateCarePlan(ActiveCarePlanRequestDTO requestDTO) {
        int id = requestDTO.carePlanId;
        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(id);
        carePlanEntity.approve();
        this.carePlanRepository.updateOne(carePlanEntity);
        return new ActiveCarePlanResponseDTO(carePlanEntity.getId(), carePlanEntity.getStatus().toString(),
                carePlanEntity.getUpdatedAt().toString());
    }

    @Override
    public DiscontinueCarePlanResponseDTO discontinueCarePlan(DiscontinueCarePlanRequestDTO requestDTO) {
        int id = requestDTO.carePlanId;
        CarePlanEntity carePlanEntity = this.carePlanRepository.findById(id);
        carePlanEntity.archive();
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
                    output.lastReviewedBy = entity.getLastReviewBy() == null ? null : entity.getLastReviewBy();
                    output.lastReviewedDateTime = entity.getLastReviewDateTime() == null ? null : entity.getLastReviewDateTime().toString();
                    output.nextReviewDateTime = entity.getNextReviewDateTime() == null ? null : entity.getNextReviewDateTime().toString();
                    output.cycle = 90;
                    output.resident = new CarePlanOutput.CarePlanResidentOutput(
                            entity.getResident().getId(),
                            entity.getResident().getFullname(),
                            entity.getResident().getDob().toString()
                    );
                    if (entity.getResident().getRoom() == null || entity.getResident().getBed() == null) {

                        output.definition = new CarePlanOutput.CarePlanResidentDefinitionOutput(
                                "", ""
                        );

                    } else {

                        output.definition = new CarePlanOutput.CarePlanResidentDefinitionOutput(
                                entity.getResident().getRoom(),
                                entity.getResident().getBed()
                        );
                    }
                    output.LOCTier = 0;
                    output.goalCount = entity.getListCareGoal().size();
                    output.interventionCount = 0;
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
        responseDTO.lastReviewedBy = carePlanEntity.getLastReviewBy() == null ? null : carePlanEntity.getLastReviewBy();
        responseDTO.lastReviewedDateTime = carePlanEntity.getLastReviewDateTime() == null ? null : carePlanEntity.getLastReviewDateTime().toString();
        responseDTO.nextReviewDateTime = carePlanEntity.getNextReviewDateTime() == null ? null : carePlanEntity.getNextReviewDateTime().toString();
        responseDTO.cycle = 90;

        responseDTO.resident = new CarePlanOutput.CarePlanResidentOutput(
                carePlanEntity.getResident().getId(),
                carePlanEntity.getResident().getFullname().toString(),
                carePlanEntity.getResident().getDob().toString()
        );
        responseDTO.createdAt = carePlanEntity.getCreatedAt().toString();
        responseDTO.updatedAt = carePlanEntity.getUpdatedAt().toString();
        responseDTO.isDeleted = carePlanEntity.getIsDeleted();
        responseDTO.definition = new CarePlanOutput.CarePlanResidentDefinitionOutput(
                carePlanEntity.getResident().getRoom(),
                carePlanEntity.getResident().getBed()
        );
        responseDTO.goals = carePlanEntity.getListCareGoal()
                .stream()
                .map(goal -> {
                    GetCarePlanDetailResponseDTO.Goal dto = new GetCarePlanDetailResponseDTO.Goal();
                    dto.id = goal.getId();
                    dto.status = goal.getStatus().name();
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
                    dto.residentId = entity.getResident().getId();
                    dto.residentName = entity.getResident().getFullname();
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

    @Override
    public CreateCarePlanResponseDTO createCarePlan(CreateCarePlanRequestDTO requestDTO, String purpose) {

        //create care plan entity
        CarePlanEntity carePlan = new CarePlanEntity();
        carePlan.setLastReviewBy(null);
        carePlan.setCreatedAt(OffsetDateTime.now());
        carePlan.setUpdatedAt(OffsetDateTime.now());
        carePlan.setIsDeleted(false);
        carePlan.setLastReviewDateTime(null);

        switch (purpose){
            case "NEED_REVIEW":
                carePlan.setStatus(CarePlanStatusEnum.PENDING_REVIEW);
            default:
                carePlan.setStatus(CarePlanStatusEnum.DRAFT);
        }

        //TODO: find resident info
        int residentId = requestDTO.residentId;
        ResidentEntity resident = this.carePlanRepository.getResidentInfo((long) residentId);

        //TODO: check chart lock ,...
        if (resident.isChartLocked() == true){
            throw new RuntimeException("Resident is locked, can not create care plan for this resident");
        }


        CarePlanResidentInfoEntity carePlanResidentInfoEntity = new CarePlanResidentInfoEntity();
        carePlanResidentInfoEntity.setId(resident.getId().intValue());
        carePlan.setResident(carePlanResidentInfoEntity);

        List<CareGoalEntity> goals = requestDTO.listCareGoal.stream()
                .map(goalDto -> {

                    CareGoalEntity goal = new CareGoalEntity();
                    goal.setName(goalDto.name);
                    goal.setDescription(goalDto.description);
                    goal.setStatus(CarePlanGoalStatusEnum.IN_PROGRESS);

                    List<CareInterventionEntity> interventions =
                            goalDto.listCareIntervention.stream()
                                    .map(interventionDto -> {
                                        CareInterventionEntity intervention =
                                                new CareInterventionEntity();

                                        intervention.setName(interventionDto.name);
                                        intervention.setAssinedRole(interventionDto.assingedRole);
                                        return intervention;
                                    })
                                    .toList();

                    goal.setListCareIntervention(interventions);

                    return goal;
                })
                .toList();

        carePlan.setListCareGoal(goals);

        System.out.println(carePlan);

        //TODO: call repo to save
        this.carePlanRepository.saveOne(carePlan);
        return null;
    }

}
