package com.eldercare.modules.careplan_management.careplan_design.entity;

import java.time.OffsetDateTime;

import com.eldercare.common.enums.CarePlanGoalStatusEnum;
import com.eldercare.common.enums.CarePlanStatusEnum;

public class CarePlanEntity {

    private int id;
    private CarePlanStatusEnum status = CarePlanStatusEnum.DRAFT;
    private Boolean significantFlag = false;
    private int residentId;
    private CarePlanGoalStatusEnum goal;
    private String assingedRole;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean isDeleted;

    public CarePlanEntity() {
    }


    public CarePlanEntity(int id, CarePlanStatusEnum status, Boolean significantFlag, int residentId,
            CarePlanGoalStatusEnum goal, String assingedRole, OffsetDateTime createdAt, OffsetDateTime updatedAt,
            Boolean isDeleted) {
        this.id = id;
        this.status = status;
        this.significantFlag = significantFlag;
        this.residentId = residentId;
        this.goal = goal;
        this.assingedRole = assingedRole;
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

    public void setGoal(CarePlanGoalStatusEnum goal) {
        this.goal = goal;
        this.updatedAt = OffsetDateTime.now();

    }

    public int getId() {
        return id;
    }

    public CarePlanStatusEnum getStatus() {
        return status;
    }

    public Boolean getSignificantFlag() {
        return significantFlag;
    }

    public int getResidentId() {
        return residentId;
    }

    public CarePlanGoalStatusEnum getGoal() {
        return goal;
    }

    public String getAssingedRole() {
        return assingedRole;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }


    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }


    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public String toString() {
        return "CarePlanEntity [id=" + id + ", status=" + status + ", significantFlag=" + significantFlag
                + ", residentId=" + residentId + ", goal=" + goal + ", assingedRole=" + assingedRole + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", isDeleted=" + isDeleted + "]";
    }
    

}
