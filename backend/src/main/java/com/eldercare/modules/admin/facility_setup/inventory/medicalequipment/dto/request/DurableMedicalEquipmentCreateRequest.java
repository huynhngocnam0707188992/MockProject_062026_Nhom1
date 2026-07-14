package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DurableMedicalEquipmentCreateRequest {

    @JsonProperty("item_name")
    @NotBlank(message = "Item name is required")
    private String itemName;

    @JsonProperty("category_id")
    @NotBlank
    private long categoryId;

    @JsonProperty("asset_tag")
    @NotBlank(message = "Asset tag is required")
    private String assetTag;

    @JsonProperty("facility_id")
    @NotBlank(message = "Facility ID is required")
    private long facilityId;

    @JsonProperty("unit_value")
    @NotBlank(message = "Unit value is required")
    private BigDecimal unitValue;

}
