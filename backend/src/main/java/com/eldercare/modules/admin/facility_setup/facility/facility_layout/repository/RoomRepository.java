package com.eldercare.modules.admin.facility_setup.facility.facility_layout.repository;

import com.eldercare.modules.admin.facility_setup.facility.facility_layout.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.facility WHERE r.facility.id = :facilityId")
    Page<RoomEntity> findByFacilityId(@Param("facilityId") Long facilityId, Pageable pageable);

    @Query("SELECT r FROM RoomEntity r JOIN FETCH r.facility WHERE r.facility.id = :facilityId AND LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :roomNumber, '%'))")
    Page<RoomEntity> findByFacilityIdAndRoomNumberContainingIgnoreCase(
            @Param("facilityId") Long facilityId, 
            @Param("roomNumber") String roomNumber, 
            Pageable pageable);

    boolean existsByFacilityIdAndRoomNumber(Long facilityId, String roomNumber);
}
