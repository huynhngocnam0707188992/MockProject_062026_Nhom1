package com.eldercare.modules.admin.facility_setup.inventory.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InventoryCategoryRequest {
    
    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Description is required")
    private String description;
}
