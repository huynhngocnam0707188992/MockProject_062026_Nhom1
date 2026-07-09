package com.eldercare.modules.facility.service;

import com.eldercare.modules.facility.dto.request.BedRequest;
import com.eldercare.modules.facility.dto.request.RoomRequest;
import com.eldercare.modules.facility.dto.response.BedResponse;
import com.eldercare.modules.facility.dto.response.RoomResponse;

import java.util.List;

public interface RoomBedService {
    List<RoomResponse> getRoomList(Long facilityId);
    RoomResponse createRoom(Long facilityId, RoomRequest request);
    RoomResponse updateRoom(Long roomId, RoomRequest request);
    void deleteRoom(Long roomId);

    List<BedResponse> getBedListByRoomId(Long roomId);
    BedResponse createBed(Long roomId, BedRequest request);
    BedResponse updateBedStatus(Long bedId, BedRequest request);
}