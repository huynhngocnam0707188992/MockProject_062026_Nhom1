package com.eldercare.modules.resident_intake.care_level.admin.repository;

import com.eldercare.modules.resident_intake.care_level.CareLevelRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;

public interface CareLevelRateRepository 
        extends JpaRepository<CareLevelRateEntity, Long> {

    List<CareLevelRateEntity> findByCareLevel_Id(Long careLevelId);

}