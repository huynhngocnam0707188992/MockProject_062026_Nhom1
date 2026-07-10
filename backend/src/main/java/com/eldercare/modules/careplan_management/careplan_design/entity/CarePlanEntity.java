package com.eldercare.modules.careplan_management.careplan_design.entity;

import java.util.Date;

import com.eldercare.common.enums.CarePlanGoalStatusEnum;
import com.eldercare.common.enums.CarePlanStatusEnum;

public class CarePlanEntity {

    private int id;
    private CarePlanStatusEnum status = CarePlanStatusEnum.DRAFT;
    private Boolean significantFlag = false;
    private int residentId;
    private CarePlanGoalStatusEnum goal;
    private String assingedRole;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;

   

    public CarePlanEntity(int id, CarePlanStatusEnum status, Boolean significantFlag, int residentId,
            CarePlanGoalStatusEnum goal, String assingedRole, Date createdAt, Date updatedAt, Boolean isDeleted) {
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
    }

    public void discontinue() {
        if (this.status == CarePlanStatusEnum.DISCONTINUED) {
            throw new RuntimeException("This care plan is already discontinued");
        }
        this.status = CarePlanStatusEnum.DISCONTINUED;
    }

    public void markSignificant() {
        if (this.significantFlag == false) {
            this.significantFlag = true;
        }
    }

    public void setGoal(CarePlanGoalStatusEnum goal) {
        this.goal = goal;
    }
}
