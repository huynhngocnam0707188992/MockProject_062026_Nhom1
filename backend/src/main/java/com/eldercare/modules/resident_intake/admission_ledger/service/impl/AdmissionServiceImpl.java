package com.eldercare.modules.resident_intake.admission_ledger.service.impl;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eldercare.exception.custom.BadRequestException;
import com.eldercare.exception.custom.ResourceNotFoundException;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.FacilityEntity;
import com.eldercare.modules.resident_intake.admission_ledger.AdmissionEntity;
import com.eldercare.modules.resident_intake.admission_ledger.dto.request.AdmissionCreateRequest;
import com.eldercare.modules.resident_intake.admission_ledger.dto.request.AdmissionDischargeRequest;
import com.eldercare.modules.resident_intake.admission_ledger.dto.response.AdmissionResponse;
import com.eldercare.modules.resident_intake.admission_ledger.mapper.AdmissionMapper;
import com.eldercare.modules.resident_intake.admission_ledger.repository.AdmissionRepository;
import com.eldercare.modules.resident_intake.admission_ledger.service.AdmissionService;
import com.eldercare.modules.resident_intake.assessment.AssessmentEntity;
import com.eldercare.modules.resident_intake.assessment.repository.AssessmentRepository;
import com.eldercare.modules.resident_intake.resident.repository.ResidentRepository;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

  private final AdmissionRepository admRepo;
  private final AssessmentRepository assessRepo;
  private final ResidentRepository residentRepo;
  private final AdmissionMapper mapper;

  @Override
  @Transactional
  public AdmissionResponse create(AdmissionCreateRequest req) {
    AssessmentEntity assess = assessRepo.findById(req.getAssessmentId())
        .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

    // Admission can only be created from a completed, current assessment
    if (!"COMPLETED".equals(assess.getStatus())
        || !Boolean.TRUE.equals(assess.getIsCurrent())) {
      throw new BadRequestException("The assessment is not eligible for admission creation.");
    }

    Long residentId = assess.getResident().getId();

    // Close out any previous admission record for this resident
    admRepo.findByResidentIdAndIsCurrentTrue(residentId)
        .ifPresent(old -> {
          old.setIsCurrent(false);
          admRepo.save(old);
        });

    AdmissionEntity adm = new AdmissionEntity();
    adm.setResident(assess.getResident());
    adm.setAssessment(assess);

    FacilityEntity f = new FacilityEntity();
    f.setId(req.getFacilityId());
    adm.setFacility(f);

    adm.setAdmissionDate(req.getAdmissionDate());
    adm.setIsCurrent(true);
    adm.setCreatedAt(OffsetDateTime.now());

    admRepo.save(adm);

    // Sync resident status to reflect active admission
    ResidentEntity r = assess.getResident();
    r.setStatus("ACTIVE");
    residentRepo.save(r);

    return mapper.toResponse(adm);
  }

  @Override
  public Page<AdmissionResponse> listPaged(Pageable pageable) {
    return admRepo.findAll(pageable).map(mapper::toResponse);
  }

  @Override
  @Transactional
  public AdmissionResponse discharge(Long id, AdmissionDischargeRequest req) {
    AdmissionEntity adm = admRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));

    adm.setDischargeDate(req.getDischargeDate());
    adm.setDischargeReason(req.getDischargeReason());
    adm.setIsCurrent(false);

    admRepo.save(adm);

    // Sync resident status to reflect discharge
    ResidentEntity r = adm.getResident();
    r.setStatus("DISCHARGED");
    residentRepo.save(r);

    return mapper.toResponse(adm);
  }
}