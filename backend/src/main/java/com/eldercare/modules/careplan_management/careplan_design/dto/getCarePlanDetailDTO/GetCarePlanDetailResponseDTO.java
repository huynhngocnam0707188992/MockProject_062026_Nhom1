package com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO;

import java.util.List;

public class GetCarePlanDetailResponseDTO {
    public int id;
    public String status;
    public Boolean significantFlag;
    public int residentId;
    public List<Goal> goals;
    public List<Intervention> interventions ;
    public String createdAt;
    public String updatedAt;
    public Boolean isDeleted;

    public static class Goal {
        public int id;
        public String status;
    }

    public static class Intervention {
        public int id;
        public String assignedRole;
        public int taskCount;
    }
}
