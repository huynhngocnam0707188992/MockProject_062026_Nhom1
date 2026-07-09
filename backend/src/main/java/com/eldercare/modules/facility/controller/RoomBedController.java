package com.eldercare.modules.facility.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.modules.facility.dto.request.BedRequest;
import com.eldercare.modules.facility.dto.request.RoomRequest;
import com.eldercare.modules.facility.dto.response.BedResponse;
import com.eldercare.modules.facility.dto.response.RoomResponse;
import com.eldercare.modules.facility.service.RoomBedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomBedController {

    private final RoomBedService roomBedService;

    // Room Endpoints

    @GetMapping(RouteConstants.API_ADMIN_ROOMS)
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<List<RoomResponse>> getRoomList() {
        // Mocking facilityId = 1 based on the simplified assumption for this mock project
        return ResponseEntity.ok(roomBedService.getRoomList(1L));
    }

    @PostMapping(RouteConstants.API_ADMIN_ROOMS)
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomBedService.createRoom(1L, request));
    }

    @PutMapping(RouteConstants.API_ADMIN_ROOMS + "/{roomId}")
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomBedService.updateRoom(roomId, request));
    }

    @DeleteMapping(RouteConstants.API_ADMIN_ROOMS + "/{roomId}")
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomBedService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    // Bed Endpoints

    @GetMapping(RouteConstants.API_ADMIN_ROOMS + "/{roomId}/beds")
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<List<BedResponse>> getBedListByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomBedService.getBedListByRoomId(roomId));
    }

    @PostMapping(RouteConstants.API_ADMIN_ROOMS + "/{roomId}/beds")
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<BedResponse> createBed(@PathVariable Long roomId, @RequestBody BedRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomBedService.createBed(roomId, request));
    }

    @PutMapping(RouteConstants.API_ADMIN_BEDS + "/{bedId}")
    @PreAuthorize("hasRole('NHA_ADMIN')")
    public ResponseEntity<BedResponse> updateBedStatus(@PathVariable Long bedId, @RequestBody BedRequest request) {
        return ResponseEntity.ok(roomBedService.updateBedStatus(bedId, request));
    }
}