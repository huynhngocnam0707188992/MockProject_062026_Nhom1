package com.eldercare.modules.careplan_management.careplan_design.entity;

import java.time.OffsetDateTime;
import java.util.List;

import com.eldercare.common.enums.CarePlanStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarePlanEntity {

    private int id;
    private CarePlanStatusEnum status = CarePlanStatusEnum.DRAFT;
    private Boolean significantFlag = false;
    private int residentId;
    private List<CareGoalEntity> listCareGoal;
    private List<CareInterventionEntity> listCareIntervention;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean isDeleted;

    public CarePlanEntity() {
    }

    public CarePlanEntity(int id, CarePlanStatusEnum status, Boolean significantFlag, int residentId,
            List<CareGoalEntity> listCareGoal, List<CareInterventionEntity> listCareIntervention,
            OffsetDateTime createdAt, OffsetDateTime updatedAt,
            Boolean isDeleted) {
        this.id = id;
        this.status = status;
        this.significantFlag = significantFlag;
        this.residentId = residentId;
        this.listCareGoal = listCareGoal;
        this.listCareIntervention = listCareIntervention;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public void active() {
        if (!(this.status == CarePlanStatusEnum.DRAFT)) {
            throw new RuntimeException("This care plan is not in the status that can be active");
        }
        this.status = CarePlanStatusEnum.ACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }

    public void discontinue() {
        if (this.status == CarePlanStatusEnum.DISCONTINUED) {
            throw new RuntimeException("This care plan is already discontinued");
        }
        this.status = CarePlanStatusEnum.DISCONTINUED;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markSignificant() {
        if (this.significantFlag == false) {
            this.significantFlag = true;
            this.updatedAt = OffsetDateTime.now();
        }
    }

    // public void setGoal(CarePlanGoalStatusEnum goal) {
    // this.goal = goal;
    // this.updatedAt = OffsetDateTime.now();
    // }

}
