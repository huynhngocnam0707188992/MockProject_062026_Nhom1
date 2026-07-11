package com.eldercare.modules.careplan_management.careplan_design.entity;

import com.eldercare.common.enums.CarePlanGoalStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareGoalEntity {
    private int id;
    private CarePlanGoalStatusEnum status;

    public CareGoalEntity(int id, CarePlanGoalStatusEnum status) {
        this.id = id;
        this.status = status;
    }

    public CareGoalEntity() {
    }

    @Override
    public String toString() {
        return "CareGoalEntity [id=" + id + ", status=" + status + "]";
    }

}
