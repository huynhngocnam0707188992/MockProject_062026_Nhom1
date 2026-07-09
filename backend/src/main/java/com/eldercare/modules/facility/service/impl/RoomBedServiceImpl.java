package com.eldercare.modules.facility.service.impl;

import com.eldercare.common.enums.BedStatus;
import com.eldercare.exception.custom.BadRequestException;
import com.eldercare.exception.custom.ResourceNotFoundException;
import com.eldercare.modules.facility.dto.request.BedRequest;
import com.eldercare.modules.facility.dto.request.RoomRequest;
import com.eldercare.modules.facility.dto.response.BedResponse;
import com.eldercare.modules.facility.dto.response.RoomResponse;
import com.eldercare.modules.facility.entity.Bed;
import com.eldercare.modules.facility.entity.Facility;
import com.eldercare.modules.facility.entity.Room;
import com.eldercare.modules.facility.mapper.RoomBedMapper;
import com.eldercare.modules.facility.repository.BedRepository;
import com.eldercare.modules.facility.repository.FacilityRepository;
import com.eldercare.modules.facility.repository.RoomRepository;
import com.eldercare.modules.facility.service.RoomBedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomBedServiceImpl implements RoomBedService {

    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final FacilityRepository facilityRepository;
    private final RoomBedMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomList(Long facilityId) {
        List<Room> rooms = roomRepository.findByFacilityId(facilityId);
        return mapper.toRoomResponseList(rooms);
    }

    @Override
    @Transactional
    public RoomResponse createRoom(Long facilityId, RoomRequest request) {
        if (roomRepository.existsByFacilityIdAndRoomNumber(facilityId, request.getRoomNumber())) {
            throw new BadRequestException("Room number already exists in this facility");
        }

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        Room room = mapper.toEntity(request);
        room.setFacility(facility);
        room = roomRepository.save(room);
        return mapper.toResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.getRoomNumber().equals(request.getRoomNumber()) &&
                roomRepository.existsByFacilityIdAndRoomNumber(room.getFacility().getId(), request.getRoomNumber())) {
            throw new BadRequestException("Room number already exists in this facility");
        }

        room.setRoomNumber(request.getRoomNumber());
        if (request.getRoomType() != null) {
            room.setRoomType(request.getRoomType());
        }
        
        room = roomRepository.save(room);
        return mapper.toResponse(room);
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (bedRepository.existsByRoomIdAndStatus(roomId, BedStatus.OCCUPIED)) {
            throw new BadRequestException("Cannot delete room. It contains occupied beds.");
        }

        roomRepository.delete(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponse> getBedListByRoomId(Long roomId) {
        List<Bed> beds = bedRepository.findByRoomId(roomId);
        return mapper.toBedResponseList(beds);
    }

    @Override
    @Transactional
    public BedResponse createBed(Long roomId, BedRequest request) {
        if (bedRepository.existsByRoomIdAndBedNumber(roomId, request.getBedNumber())) {
            throw new BadRequestException("Bed number already exists in this room");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Bed bed = mapper.toEntity(request);
        bed.setRoom(room);
        bed = bedRepository.save(bed);
        return mapper.toResponse(bed);
    }

    @Override
    @Transactional
    public BedResponse updateBedStatus(Long bedId, BedRequest request) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found"));

        // Here we just update the status as per requirement. The complex check if a resident is linked
        // is typically handled when integrating with the Resident module (M1).
        if (request.getStatus() != null) {
            bed.setStatus(request.getStatus());
        }
        if (request.getBedNumber() != null && !request.getBedNumber().equals(bed.getBedNumber())) {
            if (bedRepository.existsByRoomIdAndBedNumber(bed.getRoom().getId(), request.getBedNumber())) {
                throw new BadRequestException("Bed number already exists in this room");
            }
            bed.setBedNumber(request.getBedNumber());
        }

        bed = bedRepository.save(bed);
        return mapper.toResponse(bed);
    }

    @Override
    @Transactional
    public void deleteBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found"));

        if (bed.getStatus() == BedStatus.OCCUPIED) {
            throw new BadRequestException("Cannot delete an occupied bed.");
        }

        bedRepository.delete(bed);
    }
}