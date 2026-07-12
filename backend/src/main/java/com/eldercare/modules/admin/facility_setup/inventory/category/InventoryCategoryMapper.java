package com.eldercare.modules.admin.facility_setup.inventory.category;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryCategoryMapper {
    
    InventoryCategoryMapper INSTANCE = Mappers.getMapper(InventoryCategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    InventoryCategoryEntity toEntity(InventoryCategoryRequest request);


    InventoryCategoryResponse toResponse(InventoryCategoryEntity entity);
}
