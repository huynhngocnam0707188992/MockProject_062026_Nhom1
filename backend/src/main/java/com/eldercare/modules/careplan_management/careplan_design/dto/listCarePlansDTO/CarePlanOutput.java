package com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO;

import java.util.Map;

public class CarePlanOutput {

    public int id;
    public String status;
    public Boolean significantFlag;
    public int residentId;
    // public String goal;
    // public String assingedRole;
    public int goalCount;
    public int interventionCount;
    public String createdAt;
    public String updatedAt;
    public Boolean isDeleted;
}