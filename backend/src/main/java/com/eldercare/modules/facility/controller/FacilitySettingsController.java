package com.eldercare.modules.facility.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;
import com.eldercare.modules.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RouteConstants.API_ADMIN_FACILITY_SETTINGS)
@RequiredArgsConstructor
public class FacilitySettingsController {

    private final FacilityService facilityService;

    @GetMapping
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<FacilityResponse> getFacilityInfo() {
        return ResponseEntity.ok(facilityService.getFacilityInfo());
    }

    @PutMapping
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<FacilityResponse> updateFacilityInfo(@RequestBody FacilityUpdateRequest request) {
        return ResponseEntity.ok(facilityService.updateFacilityInfo(request));
    }
}