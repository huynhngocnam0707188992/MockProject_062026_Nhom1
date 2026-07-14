package com.eldercare.modules.resident_intake.pre_admission.mapper;

import org.springframework.stereotype.Component;

import com.eldercare.modules.resident_intake.pre_admission.PreAdmissionScreeningEntity;
import com.eldercare.modules.resident_intake.pre_admission.dto.response.PreResponse;
import com.eldercare.modules.resident_intake.pre_admission.dto.response.PreSelectDTO;

@Component
public class PreAdmissionScreeningMapper {
  public PreResponse toResponse(PreAdmissionScreeningEntity p) {
    PreResponse dto = new PreResponse();
    dto.setId(p.getId());
    dto.setStatus(p.getStatus());
    dto.setResidentId(p.getResident().getId());
    dto.setResidentName(p.getResident().getFirstName() + " " + p.getResident().getLastName());
    dto.setCreatedAt(p.getCreatedAt());

    return dto;
  }

  public PreSelectDTO toSelectDTO(PreAdmissionScreeningEntity p) {
    PreSelectDTO dto = new PreSelectDTO();
    dto.setId(p.getId());
    dto.setResidentName(p.getResident().getFirstName() + " " + p.getResident().getLastName());
    return dto;
  }
}
