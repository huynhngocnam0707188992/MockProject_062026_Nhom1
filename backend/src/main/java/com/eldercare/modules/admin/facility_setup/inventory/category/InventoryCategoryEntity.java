package com.eldercare.modules.admin.facility_setup.inventory.category;

import java.time.OffsetDateTime;
import java.util.List;

import com.eldercare.modules.admin.facility_setup.inventory.consumablesupplies.ConsumableSupplyEntity;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.DurableMedicalEquipmentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "inventory_categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InventoryCategoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private long id;
  @Column(name = "category_name", nullable = false, unique = true, length = 100)
  private String categoryName;

  @Column(length = 255)
  private String description;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @OneToMany(mappedBy = "category")
  private List<ConsumableSupplyEntity> consumableSupplies;

  @OneToMany(mappedBy = "category")
  private List<DurableMedicalEquipmentEntity> durableMedicalEquipments;

}
