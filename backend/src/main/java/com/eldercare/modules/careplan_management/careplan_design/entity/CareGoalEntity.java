package com.eldercare.modules.careplan_management.careplan_design.entity;

import com.eldercare.common.enums.CarePlanGoalStatusEnum;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CareGoalEntity {
    private int id;
    private String name;
    private String description;
    private CarePlanGoalStatusEnum status;
    private List<CareInterventionEntity> listCareIntervention = new ArrayList<>();

    public void addIntervention(CareInterventionEntity careInterventionEntity) {
        this.listCareIntervention.add(careInterventionEntity);
    }

    public void removeIntervention(int id){
        for (int i = 0; i < this.listCareIntervention.size(); i++) {
            if (this.listCareIntervention.get(i).getId() == id){
                this.listCareIntervention.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "CareGoalEntity [id=" + id + ", status=" + status + "]";
    }

}
