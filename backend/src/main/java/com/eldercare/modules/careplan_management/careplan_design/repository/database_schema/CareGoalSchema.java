package com.eldercare.modules.careplan_management.careplan_design.repository.database_schema;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eldercare.common.enums.CarePlanGoalStatusEnum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "care_goals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CareGoalSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarePlanGoalStatusEnum status;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_plan_id", nullable = false)
    private CarePlanSchema carePlan;

    @OneToMany(
            mappedBy = "careGoal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CareInterventionSchema> listCareIntervention = new ArrayList<>();
}
