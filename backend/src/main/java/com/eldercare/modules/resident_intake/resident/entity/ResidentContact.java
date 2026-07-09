package com.eldercare.modules.resident_intake.resident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "resident_contacts")
@Getter
@Setter
public class ResidentContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "relationship_type", nullable = false)
    private String relationshipType;

    @Column(name = "is_guarantor", nullable = false)
    private boolean isGuarantor = false;

    @Column(name = "is_emergency_contact", nullable = false)
    private boolean isEmergencyContact = false;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
