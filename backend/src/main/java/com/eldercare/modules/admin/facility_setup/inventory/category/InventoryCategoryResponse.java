package com.eldercare.modules.admin.facility_setup.inventory.category;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryCategoryResponse {
    
    private long id;
    private String categoryName;
    private String description;
    private OffsetDateTime createdAt;
}
