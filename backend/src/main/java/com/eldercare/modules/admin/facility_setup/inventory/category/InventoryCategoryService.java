package com.eldercare.modules.admin.facility_setup.inventory.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eldercare.common.dto.PagedResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryCategoryService {

    private final InventoryCategoryRepository inventoryCategoryRepository;
    private final InventoryCategoryMapper inventoryCategoryMapper;

    public PagedResponse<List<InventoryCategoryResponse>> getInventoryCategoryList(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryCategoryEntity> categoryPage = inventoryCategoryRepository.findAll(pageable);
        List<InventoryCategoryResponse> content = categoryPage.getContent()
                .stream()
                .map(inventoryCategoryMapper::toResponse)
                .collect(Collectors.toList());
        return PagedResponse.of(content, 200, "Inventory categories retrieved successfully",
                page, categoryPage.getTotalPages(), size, categoryPage.getTotalElements());
    }

    @Transactional
    public InventoryCategoryResponse createInventoryCategory(InventoryCategoryRequest request) {
        InventoryCategoryEntity entity = inventoryCategoryMapper.toEntity(request);
        InventoryCategoryEntity savedEntity = inventoryCategoryRepository.save(entity);
        return inventoryCategoryMapper.toResponse(savedEntity);
    }

    public InventoryCategoryResponse getInventoryCategoryById(long id) {
        InventoryCategoryEntity entity = inventoryCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory category not found with id: " + id));
        return inventoryCategoryMapper.toResponse(entity);
    }

    @Transactional
    public InventoryCategoryResponse updateInventoryCategory(long id, InventoryCategoryRequest request) {
        InventoryCategoryEntity entity = inventoryCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory category not found with id: " + id));
        entity.setCategoryName(request.getCategoryName());
        entity.setDescription(request.getDescription());
        InventoryCategoryEntity updatedEntity = inventoryCategoryRepository.save(entity);
        return inventoryCategoryMapper.toResponse(updatedEntity);
    }

    @Transactional
    public void deleteInventoryCategory(long id) {
        InventoryCategoryEntity entity = inventoryCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory category not found with id: " + id));
        inventoryCategoryRepository.delete(entity);
    }

}
