package com.eldercare.modules.carelevel.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.modules.carelevel.admin.entity.CareLevel;

public interface CareLevelRepository extends JpaRepository<CareLevel, Long> {
}