package com.eldercare.modules.admin.facility_setup.inventory.consumablesupplies;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.ManyToAny;

import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.FacilityEntity;
import com.eldercare.modules.admin.facility_setup.inventory.category.InventoryCategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "consumable_supplies")
@Data
public class ConsumableSupplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String itemName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private InventoryCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    private FacilityEntity facility;

    private int stockOnHand;

    private int total;

    private int reorderThreshold;

    private BigDecimal unitCost;
    
    private BigDecimal privatePayRate;

    private String status;

    private Boolean isDeleted;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


}
