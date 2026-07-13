package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request.DurableMedicalEquipmentRequest;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.response.DurableMedicalEquipmentResponse;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.entity.DurableMedicalEquipmentEntity;

@Mapper(componentModel = "spring")
public interface DurableMedicalEquipmentMapper {

    @Mapping(target = "id", ignore = true)
    DurableMedicalEquipmentEntity toEntity(DurableMedicalEquipmentRequest request);

    DurableMedicalEquipmentResponse toResponse(DurableMedicalEquipmentEntity entity);

}
