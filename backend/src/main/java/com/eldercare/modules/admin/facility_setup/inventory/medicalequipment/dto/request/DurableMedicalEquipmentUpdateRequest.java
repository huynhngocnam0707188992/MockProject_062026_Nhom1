package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DurableMedicalEquipmentUpdateRequest {
    @JsonProperty("item_name")
    @NotBlank(message = "Item name is required")
    private String itemName;

    @JsonProperty("category_id")
    @NotBlank
    private long categoryId;

    @JsonProperty("facility_id")
    @NotBlank(message = "Facility ID is required")
    private long facilityId;

    @JsonProperty("unit_value")
    @NotBlank(message = "Unit value is required")
    private BigDecimal unitValue;
}
