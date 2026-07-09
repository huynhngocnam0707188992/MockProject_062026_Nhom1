package com.eldercare.modules.facility.repository;

import com.eldercare.modules.facility.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByFacilityId(Long facilityId);
    boolean existsByFacilityIdAndRoomNumber(Long facilityId, String roomNumber);
}