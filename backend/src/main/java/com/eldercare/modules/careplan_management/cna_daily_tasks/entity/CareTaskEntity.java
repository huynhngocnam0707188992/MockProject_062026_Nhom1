package com.eldercare.modules.careplan_management.cna_daily_tasks.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "care_tasks")
public class CareTask {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "task_type", nullable = false, length = 50)
private java.lang.String taskType;

@jakarta.validation.constraints.Size(max = 20)
@jakarta.validation.constraints.NotNull
@org.hibernate.annotations.ColumnDefault("'PENDING'")
@jakarta.persistence.Column(name = "status", nullable = false, length = 20)
private java.lang.String status;

@jakarta.validation.constraints.NotNull
@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "is_abnormal_flagged", nullable = false)
private java.lang.Boolean isAbnormalFlagged;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "care_intervention_id", nullable = false)
private com.eldercare.modules.careplan_management.careplan_design.repository.database_schema.CareInterventionSchema careIntervention;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "assigned_cna_id")
private com.eldercare.modules.admin.user_management.UserEntity assignedCna;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "scheduled_time", nullable = false)
private java.time.OffsetDateTime scheduledTime;

@jakarta.persistence.Column(name = "completed_at")
private java.time.OffsetDateTime completedAt;

@org.hibernate.annotations.Nationalized
@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "goal")
private java.lang.String goal;



}