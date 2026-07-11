package com.eldercare.modules.careplan_management.careplan_design.repository.database_schema;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "care_plans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarePlanSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "significant_change_flag", nullable = false)
    private Boolean significantChangeFlag = false;

    @Column(name = "resident_id", nullable = false)
    private Long residentId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareGoalSchema> listCareGoal = new ArrayList<>();

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareInterventionSchema> listCareIntervention = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}