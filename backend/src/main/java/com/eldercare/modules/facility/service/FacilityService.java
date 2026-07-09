package com.eldercare.modules.facility.service;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;

import java.util.List;

public interface FacilityService {
    PagedResponse<List<FacilityResponse>> getFacilities(int page, int size, String search);
    FacilityResponse createFacility(FacilityCreateRequest request);
    FacilityResponse getFacilityInfo(Long facilityId);
    FacilityResponse updateFacilityInfo(Long facilityId, FacilityUpdateRequest request);
}
