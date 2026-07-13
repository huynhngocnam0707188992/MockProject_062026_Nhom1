package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DurableMedicalEquipmentRequest {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("category_id")
    private long categoryId;

    @JsonProperty("asset_tag")
    private String assetTag;

    @JsonProperty("facility_id")
    private long facilityId;

    @JsonProperty("assigned_to_user")
    private long assignedToUserId;

    @JsonProperty("unit_value")
    private BigDecimal unitValue;

    private String status;
}
