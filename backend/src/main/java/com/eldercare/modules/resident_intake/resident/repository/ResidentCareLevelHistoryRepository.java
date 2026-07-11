package com.eldercare.modules.resident_intake.resident.repository;


import com.eldercare.modules.admin.facility_setup.care_level.entity.ResidentCareLevelHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentCareLevelHistoryRepository extends JpaRepository<ResidentCareLevelHistoryEntity, Long> {
    List<ResidentCareLevelHistoryEntity> findByResidentId(Long residentId);
}

