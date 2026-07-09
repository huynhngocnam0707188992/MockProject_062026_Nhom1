package com.eldercare.modules.staffing.service;

import com.eldercare.modules.staffing.dto.request.UpdateStaffingRatioRequest;
import com.eldercare.modules.staffing.dto.response.StaffingRatioResponse;

public interface StaffingRatioService {

    StaffingRatioResponse getStaffingRatio();

    StaffingRatioResponse updateStaffingRatio(
            UpdateStaffingRatioRequest request
    );

}