package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.service.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.FacilityEntity;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.repository.FacilityRepository;
import com.eldercare.modules.admin.facility_setup.inventory.category.entity.InventoryCategoryEntity;
import com.eldercare.modules.admin.facility_setup.inventory.category.repository.InventoryCategoryRepository;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.mapper.DurableMedicalEquipmentMapper;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request.DurableMedicalEquipmentRequest;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.response.DurableMedicalEquipmentResponse;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.entity.DurableMedicalEquipmentEntity;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.enums.DurableMedicalEquipmentEnum;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.repository.DurableMedicalEquipmentRepository;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.service.DurableMedicalEquipmentServiceInterface;
import com.eldercare.modules.admin.user_management.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DurableMedicalEquipmentServiceImpl implements DurableMedicalEquipmentServiceInterface {

    private final DurableMedicalEquipmentRepository durableMedicalEquipmentRepository;

    private final DurableMedicalEquipmentMapper durableMedicalEquipmentMapper;

    private final InventoryCategoryRepository inventoryCategoryRepository;

    private final FacilityRepository facilityRepository;

    @Override
    public PagedResponse<List<DurableMedicalEquipmentResponse>> getAllDurableMedicalEquipment(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DurableMedicalEquipmentEntity> equipmentPage = durableMedicalEquipmentRepository
                .findByPageableAndIsDeletedFalse(pageable);
        List<DurableMedicalEquipmentResponse> content = equipmentPage.getContent()
                .stream()
                .map(durableMedicalEquipmentMapper::toResponse)
                .toList();

        return PagedResponse.of(content, 200, "Durable medical equipment retrieved successfully",
                page, equipmentPage.getTotalPages(), size, equipmentPage.getTotalElements());
    }

    @Override
    public DurableMedicalEquipmentResponse createEquipment(
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest) {
        if (durableMedicalEquipmentRepository
                .findByAssetTagAndIsDeletedFalse(durableMedicalEquipmentRequest.getAssetTag()).isPresent()) {
            throw new RuntimeException("Asset tag already exists");
        }
        DurableMedicalEquipmentEntity entity = durableMedicalEquipmentMapper.toEntity(durableMedicalEquipmentRequest);
        DurableMedicalEquipmentEntity savedEntity = durableMedicalEquipmentRepository.save(entity);
        return durableMedicalEquipmentMapper.toResponse(savedEntity);
    }

    @Override
    public DurableMedicalEquipmentResponse getEquipmentById(Long id) {
        DurableMedicalEquipmentEntity entity = durableMedicalEquipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Durable medical equipment not found with id: " + id));
        return durableMedicalEquipmentMapper.toResponse(entity);
    }

    @Override
    public DurableMedicalEquipmentResponse updateEquipment(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest) {
        DurableMedicalEquipmentEntity entity = durableMedicalEquipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Durable medical equipment not found with id: " + id));
        long newCategoryId = durableMedicalEquipmentRequest.getCategoryId();
        InventoryCategoryEntity newCategory = inventoryCategoryRepository.getReferenceById(newCategoryId);
        long newFacilityId = durableMedicalEquipmentRequest.getFacilityId();
        FacilityEntity newFacility = facilityRepository.getReferenceById(newFacilityId);

        entity.setItemName(durableMedicalEquipmentRequest.getItemName());
        entity.setCategory(newCategory);
        entity.setFacility(newFacility);
        entity.setUnitValue(durableMedicalEquipmentRequest.getUnitValue());
        DurableMedicalEquipmentEntity updatedEntity = durableMedicalEquipmentRepository.save(entity);
        return durableMedicalEquipmentMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteEquipment(Long id) {
        if (durableMedicalEquipmentRepository
                .findByIdAndStatusAndIsDeletedFalse(id, DurableMedicalEquipmentEnum.RETIRED.toString())
                .isPresent()) {
            DurableMedicalEquipmentEntity entityToDelete = durableMedicalEquipmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Durable medical equipment not found with id: " + id));
            entityToDelete.setIsDeleted(true);
            durableMedicalEquipmentRepository.save(entityToDelete);
        } else {
            throw new RuntimeException("Durable medical equipment cannot be deleted unless it is retired");
        }
    }

    @Override
    public DurableMedicalEquipmentResponse patchEquipmentStatus(long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest) {

        if (durableMedicalEquipmentRepository
                .findByIdAndStatusAndIsDeletedFalse(id, DurableMedicalEquipmentEnum.RETIRED.toString()).isPresent()) {
            throw new RuntimeException("Durable medical equipment cannot be updated as it is retired");
        }

        DurableMedicalEquipmentEntity entity = durableMedicalEquipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Durable medical equipment not found with id: " + id));
        DurableMedicalEquipmentEnum newStatus = DurableMedicalEquipmentEnum
                .valueOf(durableMedicalEquipmentRequest.getStatus().toUpperCase().trim());
        entity.setStatus(newStatus);
        DurableMedicalEquipmentEntity updatedEntity = durableMedicalEquipmentRepository.save(entity);
        return durableMedicalEquipmentMapper.toResponse(updatedEntity);
    }

    @Override
    public DurableMedicalEquipmentResponse assignEquipmentForUser(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest) {
         if (durableMedicalEquipmentRepository
                .findByIdAndStatusAndIsDeletedFalse(id, DurableMedicalEquipmentEnum.AVAILABLE.toString()).isPresent()) {
            DurableMedicalEquipmentEntity entity = durableMedicalEquipmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Durable medical equipment not found with id: " + id)); 
            long newAssignedToUserId = durableMedicalEquipmentRequest.getAssignedToUserId();
            UserEntity newAssignedToUser = userRepository.getReferenceById(newAssignedToUserId);
            entity.setAssignedToUserId(durableMedicalEquipmentRequest.getAssignedToUserId());
        }
    }

    @Override
    public DurableMedicalEquipmentResponse unassignEquipmentForUser(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unassignEquipmentForUser'");
    }

}
