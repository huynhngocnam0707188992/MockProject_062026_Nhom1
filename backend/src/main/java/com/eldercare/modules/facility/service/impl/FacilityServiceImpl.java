package com.eldercare.modules.facility.service.impl;

import com.eldercare.exception.custom.BadRequestException;
import com.eldercare.exception.custom.ResourceNotFoundException;
import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;
import com.eldercare.modules.facility.entity.Facility;
import com.eldercare.modules.facility.mapper.FacilityMapper;
import com.eldercare.modules.facility.repository.FacilityRepository;
import com.eldercare.modules.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponse> getFacilities() {
        return facilityRepository.findAll()
                .stream()
                .map(facilityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FacilityResponse createFacility(FacilityCreateRequest request) {
        if (request.getTargetState() == null || !request.getTargetState().equalsIgnoreCase("CA")) {
            throw new BadRequestException("Target state must be 'CA'");
        }
        
        if (facilityRepository.findByFacilityCode(request.getFacilityCode()).isPresent()) {
            throw new BadRequestException("Facility code already exists");
        }

        Facility facility = facilityMapper.toEntity(request);
        facility = facilityRepository.save(facility);
        return facilityMapper.toResponse(facility);
    }

    @Override
    @Transactional(readOnly = true)
    public FacilityResponse getFacilityInfo(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));
        return facilityMapper.toResponse(facility);
    }

    @Override
    @Transactional
    public FacilityResponse updateFacilityInfo(Long facilityId, FacilityUpdateRequest request) {
        if (request.getTargetState() != null && !request.getTargetState().equalsIgnoreCase("CA")) {
            throw new BadRequestException("Target state must be 'CA'");
        }

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));
        
        facilityMapper.updateEntity(facility, request);
        facility = facilityRepository.save(facility);
        
        return facilityMapper.toResponse(facility);
    }
}