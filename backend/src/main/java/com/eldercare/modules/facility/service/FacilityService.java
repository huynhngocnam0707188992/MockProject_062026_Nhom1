package com.eldercare.modules.facility.service;

import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;

import java.util.List;

public interface FacilityService {
    List<FacilityResponse> getFacilities();
    FacilityResponse createFacility(FacilityCreateRequest request);
    FacilityResponse getFacilityInfo(Long facilityId);
    FacilityResponse updateFacilityInfo(Long facilityId, FacilityUpdateRequest request);
}