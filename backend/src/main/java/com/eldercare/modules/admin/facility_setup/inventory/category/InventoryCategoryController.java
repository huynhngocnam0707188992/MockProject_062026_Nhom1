package com.eldercare.modules.admin.facility_setup.inventory.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.common.dto.PagedResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
@RequiredArgsConstructor
public class InventoryCategoryController {

    private final InventoryCategoryService inventoryCategoryService;

    @GetMapping
    public ResponseEntity<PagedResponse<List<InventoryCategoryResponse>>> getAllInventoryCategories( @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inventoryCategoryService.getInventoryCategoryList(size, page));
    }

    @PostMapping
    public ResponseEntity<InventoryCategoryResponse> createInventoryCategory(
            @RequestBody InventoryCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryCategoryService.createInventoryCategory(request));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<InventoryCategoryResponse> getInventoryCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(inventoryCategoryService.getInventoryCategoryById(categoryId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<InventoryCategoryResponse> updateInventoryCategory(@PathVariable Long categoryId,
            @RequestBody InventoryCategoryRequest request) {
        return ResponseEntity.ok(inventoryCategoryService.updateInventoryCategory(categoryId, request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteInventoryCategory(@PathVariable Long categoryId) {
        inventoryCategoryService.deleteInventoryCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
