package com.eldercare.modules.human_resources.staffing_compliance.staffing.service.impl;

import com.eldercare.modules.human_resources.shift_scheduling.ShiftEntity;
import com.eldercare.modules.human_resources.shift_scheduling.StaffingShiftRequirementEntity;
import com.eldercare.modules.human_resources.shift_scheduling.repository.ShiftRepository;
import com.eldercare.modules.human_resources.shift_scheduling.repository.StaffingShiftRequirementRepository;
import com.eldercare.modules.human_resources.staffing_compliance.StaffingConfigEntity;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.request.ShiftRequirementRequest;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.request.UpdateStaffingRatioRequest;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.response.ShiftRequirementResponse;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.response.StaffingRatioResponse;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.repository.StaffingConfigRepository;
import com.eldercare.modules.human_resources.staffing_compliance.staffing.service.StaffingRatioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffingRatioServiceImpl implements StaffingRatioService {

        private static final Long DEFAULT_CONFIG_ID = 1L;

        private final StaffingConfigRepository staffingConfigRepository;
        private final StaffingShiftRequirementRepository requirementRepository;
        private final ShiftRepository shiftRepository;

        @Override
        public StaffingRatioResponse getStaffingRatio() {
                StaffingConfigEntity config = getConfig();
                return buildResponse(config);
        }

        @Override
        @Transactional
        public StaffingRatioResponse updateStaffingRatio(UpdateStaffingRatioRequest request) {
                StaffingConfigEntity config = getConfig();

                validateShiftRequirements(config, request.getShiftRequirements());

                config.setMinHrsPerResidentDay(request.getMinHrsPerResidentDay());
                config.setWarnBelowPercentage(request.getWarnBelowPercentage());
                staffingConfigRepository.save(config);

                Map<Long, StaffingShiftRequirementEntity> existingByShiftId = requirementRepository
                                .findAllByStaffingConfigId(config.getId())
                                .stream()
                                .collect(Collectors.toMap(
                                                StaffingShiftRequirementEntity::getShiftId,
                                                Function.identity()));

                List<StaffingShiftRequirementEntity> entities = request.getShiftRequirements()
                                .stream()
                                .map(item -> {
                                        StaffingShiftRequirementEntity entity = existingByShiftId
                                                        .getOrDefault(item.getShiftId(),
                                                                        StaffingShiftRequirementEntity.builder()
                                                                                        .staffingConfigId(
                                                                                                        config.getId())
                                                                                        .shiftId(item.getShiftId())
                                                                                        .build());

                                        entity.setRequiredCnaHours(item.getRequiredCnaHours());
                                        entity.setRequiredNurseHours(item.getRequiredNurseHours());
                                        return entity;
                                })
                                .toList();

                requirementRepository.saveAll(entities);
                return buildResponse(config);
        }

        private StaffingConfigEntity getConfig() {
                return staffingConfigRepository.findById(DEFAULT_CONFIG_ID)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Staffing configuration not found"));
        }

        private void validateShiftRequirements(
                        StaffingConfigEntity config,
                        List<ShiftRequirementRequest> requirements) {
                Set<Long> allowedShiftIds = shiftRepository
                                .findAllByFacilityIdOrderByStartTimeAsc(config.getFacilityId())
                                .stream()
                                .map(ShiftEntity::getId)
                                .collect(Collectors.toSet());

                Set<Long> submittedShiftIds = new HashSet<>();
                for (ShiftRequirementRequest requirement : requirements) {
                        if (!allowedShiftIds.contains(requirement.getShiftId())) {
                                throw new IllegalArgumentException(
                                                "Shift does not belong to the configured facility: "
                                                                + requirement.getShiftId());
                        }
                        if (!submittedShiftIds.add(requirement.getShiftId())) {
                                throw new IllegalArgumentException(
                                                "Duplicate shift requirement: " + requirement.getShiftId());
                        }
                }

                if (!submittedShiftIds.equals(allowedShiftIds)) {
                        throw new IllegalArgumentException(
                                        "Requirements must be supplied for every facility shift");
                }
        }

        private StaffingRatioResponse buildResponse(StaffingConfigEntity config) {
                List<ShiftEntity> shifts = shiftRepository
                                .findAllByFacilityIdOrderByStartTimeAsc(config.getFacilityId());

                Map<Long, StaffingShiftRequirementEntity> requirementsByShiftId = requirementRepository
                                .findAllByStaffingConfigId(config.getId())
                                .stream()
                                .collect(Collectors.toMap(
                                                StaffingShiftRequirementEntity::getShiftId,
                                                Function.identity()));

                List<ShiftRequirementResponse> shiftResponses = shifts.stream()
                                .map(shift -> toShiftResponse(
                                                shift,
                                                requirementsByShiftId.get(shift.getId())))
                                .toList();

                BigDecimal totalCnaHours = shiftResponses.stream()
                                .map(ShiftRequirementResponse::getRequiredCnaHours)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalNurseHours = shiftResponses.stream()
                                .map(ShiftRequirementResponse::getRequiredNurseHours)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return StaffingRatioResponse.builder()
                                .id(config.getId())
                                .facilityId(config.getFacilityId())
                                .minHrsPerResidentDay(config.getMinHrsPerResidentDay())
                                .warnBelowPercentage(config.getWarnBelowPercentage())
                                .shiftRequirements(shiftResponses)
                                .totalCnaHours(totalCnaHours)
                                .totalNurseHours(totalNurseHours)
                                .sumOfShifts(totalCnaHours.add(totalNurseHours))
                                .build();
        }

        private ShiftRequirementResponse toShiftResponse(
                        ShiftEntity shift,
                        StaffingShiftRequirementEntity requirement) {
                BigDecimal cnaHours = requirement == null
                                ? BigDecimal.ZERO
                                : requirement.getRequiredCnaHours();
                BigDecimal nurseHours = requirement == null
                                ? BigDecimal.ZERO
                                : requirement.getRequiredNurseHours();

                return ShiftRequirementResponse.builder()
                                .id(requirement == null ? null : requirement.getId())
                                .shiftId(shift.getId())
                                .shiftName(shift.getShiftName())
                                .startTime(shift.getStartTime())
                                .endTime(shift.getEndTime())
                                .requiredCnaHours(cnaHours)
                                .requiredNurseHours(nurseHours)
                                .subtotal(cnaHours.add(nurseHours))
                                .build();
        }
}
