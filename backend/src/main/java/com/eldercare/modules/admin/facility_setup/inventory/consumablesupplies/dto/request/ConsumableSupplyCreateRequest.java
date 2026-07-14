package com.eldercare.modules.admin.facility_setup.inventory.consumablesupplies.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ConsumableSupplyCreateRequest {
    
    @JsonProperty("item_name")
    @NotBlank(message = "Item name is required")
    private String itemName;

    @JsonProperty("category_id")
    @NotBlank(message = "Category ID is required")
    private Long categoryId;

    @JsonProperty("facility_id")
    @NotBlank(message = "Facility ID is required")
    private Long facilityId;

    @JsonProperty("initial_stock")
    @NotBlank(message = "Initial stock is required")
    private int stockOnHand;

    @JsonProperty("reorder_threshold")
    @NotBlank(message = "Reorder threshold is required")
    private int reorderThreshold;

    @JsonProperty("unit_cost")
    @NotBlank(message = "Unit cost is required")
    private BigDecimal unitCost;

    @JsonProperty("private_pay_rate")
    @NotBlank(message = "Private pay rate is required")
    private BigDecimal privatePayRate;
}
