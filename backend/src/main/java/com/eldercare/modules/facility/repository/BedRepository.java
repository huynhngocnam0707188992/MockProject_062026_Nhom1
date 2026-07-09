package com.eldercare.modules.facility.repository;

import com.eldercare.common.enums.BedStatus;
import com.eldercare.modules.facility.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByRoomId(Long roomId);
    boolean existsByRoomIdAndBedNumber(Long roomId, String bedNumber);
    boolean existsByRoomIdAndStatus(Long roomId, BedStatus status);
}