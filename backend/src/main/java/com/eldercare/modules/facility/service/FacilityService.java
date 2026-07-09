package com.eldercare.modules.facility.service;

import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;

import java.util.List;

public interface FacilityService {
    List<FacilityResponse> getFacilities();
    FacilityResponse createFacility(FacilityCreateRequest request);
    FacilityResponse getFacilityInfo();
    FacilityResponse updateFacilityInfo(FacilityUpdateRequest request);
}