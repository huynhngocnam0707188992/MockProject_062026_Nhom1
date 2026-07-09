package com.eldercare.modules.staffing.service.impl;

import com.eldercare.modules.staffing.dto.request.UpdateStaffingRatioRequest;
import com.eldercare.modules.staffing.dto.response.StaffingRatioResponse;
import com.eldercare.modules.staffing.entity.StaffingConfig;
import com.eldercare.modules.staffing.repository.StaffingConfigRepository;
import com.eldercare.modules.staffing.service.StaffingRatioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffingRatioServiceImpl
        implements StaffingRatioService {

    private final StaffingConfigRepository repository;

    @Override
    public StaffingRatioResponse getStaffingRatio() {

        StaffingConfig config = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException("Staffing configuration not found"));

        return StaffingRatioResponse.builder()
                .id(config.getId())
                .facilityId(config.getFacilityId())
                .minHrsPerResidentDay(config.getMinHrsPerResidentDay())
                .warnBelowPercentage(config.getWarnBelowPercentage())
                .build();
    }

    @Override
    public StaffingRatioResponse updateStaffingRatio(
            UpdateStaffingRatioRequest request) {

        StaffingConfig config = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException("Staffing configuration not found"));

        config.setMinHrsPerResidentDay(request.getMinHrsPerResidentDay());
        config.setWarnBelowPercentage(request.getWarnBelowPercentage());

        repository.save(config);

        return StaffingRatioResponse.builder()
                .id(config.getId())
                .facilityId(config.getFacilityId())
                .minHrsPerResidentDay(config.getMinHrsPerResidentDay())
                .warnBelowPercentage(config.getWarnBelowPercentage())
                .build();
    }
}