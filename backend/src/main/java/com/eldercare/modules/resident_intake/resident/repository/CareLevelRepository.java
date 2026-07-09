package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.CareLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CareLevelRepository extends JpaRepository<CareLevel, Long> {
    Optional<CareLevel> findByLevelNameAndIsDeletedFalse(String levelName);
}
