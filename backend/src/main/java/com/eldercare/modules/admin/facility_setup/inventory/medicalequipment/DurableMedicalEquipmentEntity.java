package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.FacilityEntity;
import com.eldercare.modules.admin.facility_setup.inventory.category.InventoryCategoryEntity;
import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "durable_medical_equipment")
@Data
public class DurableMedicalEquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String itemName;
    
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private InventoryCategoryEntity category;

    private String assetTag;
    
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    private FacilityEntity facility;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user", referencedColumnName = "id")
    private UserEntity assignedToUser;
    
    @ManyToOne
    @JoinColumn(name = "assigned_to_resident", referencedColumnName = "id")
    private ResidentEntity assignedToResident;

    private BigDecimal unitValue;

    private Boolean isDeleted;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
    
}
