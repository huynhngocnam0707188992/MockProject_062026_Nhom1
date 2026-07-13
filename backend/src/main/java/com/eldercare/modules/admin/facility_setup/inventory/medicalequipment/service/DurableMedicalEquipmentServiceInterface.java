package com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.service;

import java.util.List;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.request.DurableMedicalEquipmentRequest;
import com.eldercare.modules.admin.facility_setup.inventory.medicalequipment.dto.response.DurableMedicalEquipmentResponse;

public interface DurableMedicalEquipmentServiceInterface {

    PagedResponse<List<DurableMedicalEquipmentResponse>> getAllDurableMedicalEquipment(int page, int size);

    DurableMedicalEquipmentResponse createEquipment(DurableMedicalEquipmentRequest durableMedicalEquipmentRequest);

    DurableMedicalEquipmentResponse getEquipmentById(Long id);

    DurableMedicalEquipmentResponse updateEquipment(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest);

    void deleteEquipment(Long id);

    DurableMedicalEquipmentResponse patchEquipmentStatus(long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest);

    DurableMedicalEquipmentResponse assignEquipmentForUser(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest);

    DurableMedicalEquipmentResponse unassignEquipmentForUser(Long id,
            DurableMedicalEquipmentRequest durableMedicalEquipmentRequest);
}
