package com.eldercare.modules.resident_intake.resident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "insurance_providers")
@Getter
@Setter
public class InsuranceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "provider_type", nullable = false)
    private String providerType;
}
