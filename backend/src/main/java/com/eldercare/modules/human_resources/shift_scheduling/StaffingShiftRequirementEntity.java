package com.eldercare.modules.human_resources.shift_scheduling;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "staffing_shift_requirements", uniqueConstraints = @UniqueConstraint(name = "UQ_staffing_shift_requirements_config_shift", columnNames = {
        "staffing_config_id", "shift_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffingShiftRequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staffing_config_id", nullable = false)
    private Long staffingConfigId;

    @Column(name = "shift_id", nullable = false)
    private Long shiftId;

    @Column(name = "required_cna_hours", nullable = false, precision = 5, scale = 2)
    private BigDecimal requiredCnaHours;

    @Column(name = "required_nurse_hours", nullable = false, precision = 5, scale = 2)
    private BigDecimal requiredNurseHours;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (updatedAt == null) {
            updatedAt = OffsetDateTime.now();
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
