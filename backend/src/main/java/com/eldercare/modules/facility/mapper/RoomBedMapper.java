package com.eldercare.modules.facility.mapper;

import com.eldercare.modules.facility.dto.request.BedRequest;
import com.eldercare.modules.facility.dto.request.RoomRequest;
import com.eldercare.modules.facility.dto.response.BedResponse;
import com.eldercare.modules.facility.dto.response.RoomResponse;
import com.eldercare.modules.facility.entity.Bed;
import com.eldercare.modules.facility.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomBedMapper {

    public RoomResponse toResponse(Room room) {
        if (room == null) return null;
        RoomResponse res = new RoomResponse();
        res.setId(room.getId());
        res.setRoomNumber(room.getRoomNumber());
        res.setRoomType(room.getRoomType());
        res.setFacilityId(room.getFacility() != null ? room.getFacility().getId() : null);
        res.setDeleted(room.isDeleted());
        return res;
    }

    public List<RoomResponse> toRoomResponseList(List<Room> rooms) {
        return rooms.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Room toEntity(RoomRequest request) {
        if (request == null) return null;
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        return room;
    }

    public BedResponse toResponse(Bed bed) {
        if (bed == null) return null;
        BedResponse res = new BedResponse();
        res.setId(bed.getId());
        res.setBedNumber(bed.getBedNumber());
        res.setStatus(bed.getStatus());
        res.setRoomId(bed.getRoom() != null ? bed.getRoom().getId() : null);
        return res;
    }

    public List<BedResponse> toBedResponseList(List<Bed> beds) {
        return beds.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Bed toEntity(BedRequest request) {
        if (request == null) return null;
        Bed bed = new Bed();
        bed.setBedNumber(request.getBedNumber());
        bed.setStatus(request.getStatus());
        return bed;
    }
}