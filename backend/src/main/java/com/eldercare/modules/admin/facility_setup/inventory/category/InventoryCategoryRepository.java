package com.eldercare.modules.admin.facility_setup.inventory.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCategoryRepository extends JpaRepository<InventoryCategoryEntity, Long> {

    
    Page<InventoryCategoryEntity> findByPageable(Pageable pageable);
}
