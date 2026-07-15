package com.eldercare.modules.careplan_management.careplan_design.entity.resident_info;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CarePlanResidentInfoEntity {
    private int id;
    private String fullname;
    private LocalDate dob;
    private String room; // 101 102 103
    private String bed;  // A B C
    private Boolean isSignigicanChanged;
    public CarePlanResidentInfoEntity(){

    }
    public CarePlanResidentInfoEntity(int id, String fullname, LocalDate dob, String room, String bed, Boolean isSignigicanChanged) {
        this.id = id;
        this.fullname = fullname;
        this.dob = dob;
        this.room = room;
        this.bed = bed;
        this.isSignigicanChanged = isSignigicanChanged;
    }
}
