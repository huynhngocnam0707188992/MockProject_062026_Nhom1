package com.eldercare.modules.carelevel.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.carelevel.admin.entity.CareLevelRate;

import java.util.List;

public interface CareLevelRateRepository 
        extends JpaRepository<CareLevelRate, Long> {

    List<CareLevelRate> findByCareLevel_Id(Long careLevelId);

}