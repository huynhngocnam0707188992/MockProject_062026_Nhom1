package com.eldercare.modules.resident_intake.resident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "resident_sensitive_info")
@Getter
@Setter
public class ResidentSensitiveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @Column(name = "ssn_encrypted")
    private String ssnEncrypted;

    @Column(name = "medical_record_number_encrypted")
    private String medicalRecordNumberEncrypted;

    @Column(name = "primary_insurance_id_encrypted")
    private String primaryInsuranceIdEncrypted;

    @Column(name = "bank_account_encrypted")
    private String bankAccountEncrypted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
