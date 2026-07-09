package com.eldercare.modules.resident_intake.resident.repository;


import com.eldercare.modules.resident_intake.care_level.ResidentCareLevelHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentCareLevelHistoryRepository extends JpaRepository<ResidentCareLevelHistoryEntity, Long> {
    List<ResidentCareLevelHistoryEntity> findByResidentId(Long residentId);
}
