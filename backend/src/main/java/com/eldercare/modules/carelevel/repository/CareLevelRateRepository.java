package com.eldercare.modules.carelevel.repository;

import com.eldercare.modules.carelevel.entity.CareLevelRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareLevelRateRepository 
        extends JpaRepository<CareLevelRate, Long> {

    List<CareLevelRate> findByCareLevel_Id(Long careLevelId);

}