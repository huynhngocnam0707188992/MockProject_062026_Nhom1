package com.eldercare.modules.facility.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;
import com.eldercare.modules.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RouteConstants.API_ADMIN_FACILITIES)
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<List<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }

    @PostMapping
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<FacilityResponse> createFacility(@RequestBody FacilityCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.createFacility(request));
    }
}