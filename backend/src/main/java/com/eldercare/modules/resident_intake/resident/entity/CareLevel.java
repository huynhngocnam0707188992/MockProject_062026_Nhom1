package com.eldercare.modules.resident_intake.resident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "care_levels")
@Getter
@Setter
public class CareLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_code", nullable = false)
    private String levelCode;

    @Column(name = "level_name", nullable = false)
    private String levelName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
