package com.eldercare.modules.resident_intake.resident.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "facility_id", nullable = false)
    private Long facilityId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
