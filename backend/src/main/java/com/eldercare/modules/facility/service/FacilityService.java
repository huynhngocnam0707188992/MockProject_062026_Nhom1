package com.eldercare.modules.facility.service;

import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;

public interface FacilityService {
    FacilityResponse getFacilityInfo();
    FacilityResponse updateFacilityInfo(FacilityUpdateRequest request);
}