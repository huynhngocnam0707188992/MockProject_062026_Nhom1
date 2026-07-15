package com.eldercare.modules.careplan_management.careplan_design.repository.database_schema;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;
import jakarta.persistence.*;
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


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private ResidentEntity resident;


    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareGoalSchema> listCareGoal = new ArrayList<>();

    @Column(name = "last_reviewed_by", nullable = true)
    private String lastReviewdBy;

    @Column(name = "last_reviewed_datetime", nullable = true)
    private OffsetDateTime lastReviewedDateTime;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}