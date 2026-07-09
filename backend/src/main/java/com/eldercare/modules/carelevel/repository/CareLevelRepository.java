package com.eldercare.modules.carelevel.repository;

import com.eldercare.modules.carelevel.entity.CareLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareLevelRepository extends JpaRepository<CareLevel, Long> {
}