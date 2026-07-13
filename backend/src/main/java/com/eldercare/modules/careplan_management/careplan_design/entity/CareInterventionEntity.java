package com.eldercare.modules.careplan_management.careplan_design.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareInterventionEntity {
    private int id;
    private String assinedRole;

    public CareInterventionEntity(int id, String assinedRole) {
        this.id = id;
        this.assinedRole = assinedRole;
    }

    public CareInterventionEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssinedRole() {
        return assinedRole;
    }

    public void setAssinedRole(String assinedRole) {
        this.assinedRole = assinedRole;
    }

    @Override
    public String toString() {
        return "CareInterventionEntity [id=" + id + ", assinedRole=" + assinedRole + "]";
    }

}
