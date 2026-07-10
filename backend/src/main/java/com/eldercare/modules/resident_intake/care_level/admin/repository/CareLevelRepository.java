package com.eldercare.modules.resident_intake.care_level.admin.repository;

import com.eldercare.modules.resident_intake.care_level.CareLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CareLevelRepository extends JpaRepository<CareLevelEntity, Long> {
    Optional<CareLevelEntity> findByLevelNameAndIsDeletedFalse(String levelName);
}