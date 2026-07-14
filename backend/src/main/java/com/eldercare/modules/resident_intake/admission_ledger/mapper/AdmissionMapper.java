package com.eldercare.modules.resident_intake.admission_ledger.mapper;

import org.springframework.stereotype.Component;

import com.eldercare.modules.resident_intake.admission_ledger.AdmissionEntity;
import com.eldercare.modules.resident_intake.admission_ledger.dto.response.AdmissionResponse;

@Component
public class AdmissionMapper {
  public AdmissionResponse toResponse(AdmissionEntity a) {
    AdmissionResponse dto = new AdmissionResponse();
    dto.setId(a.getId());
    dto.setAdmissionDate(a.getAdmissionDate());
    dto.setResidentId(a.getResident().getId());
    dto.setResidentName(a.getResident().getFirstName() + " " + a.getResident().getLastName());
    dto.setFacilityId(a.getFacility().getId());
    dto.setAssessmentId(a.getAssessment().getId());
    dto.setDischargeDate(a.getDischargeDate());
    dto.setDischargeReason(a.getDischargeReason());
    dto.setStatus(a.getDischargeDate() == null ? "ACTIVE" : "DISCHARGED");
    return dto;
  }
}
