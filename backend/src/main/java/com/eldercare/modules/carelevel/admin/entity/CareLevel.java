package com.eldercare.modules.carelevel.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "care_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_code", nullable = false, unique = true, length = 30)
    private String levelCode;

    @Column(name = "level_name", nullable = false, length = 100)
    private String levelName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}