package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    Optional<Bed> findByBedNumberAndRoomId(String bedNumber, Long roomId);
}
