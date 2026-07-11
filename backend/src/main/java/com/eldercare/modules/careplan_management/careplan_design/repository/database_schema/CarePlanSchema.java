package com.eldercare.modules.careplan_management.careplan_design.repository.database_schema;

import java.time.OffsetDateTime;

import com.eldercare.common.enums.CarePlanStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "care_plans")
public class CarePlanSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 20)
    public String status;

    @Column(name = "significant_change_flag", nullable = false)
    public Boolean significantChangeFlag = false;

    @Column(name = "resident_id", nullable = false)
    public Long residentId;

    @Column(name = "is_deleted", nullable = false)
    public Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime updatedAt;

    public CarePlanSchema() {
    }

    public CarePlanSchema(Long id, String status, Boolean significantChangeFlag, Long residentId, Boolean isDeleted,
            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.significantChangeFlag = significantChangeFlag;
        this.residentId = residentId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}