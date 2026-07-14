package com.eldercare.modules.resident_intake.pre_admission.service.impl;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eldercare.exception.ConflictException;
import com.eldercare.exception.custom.BadRequestException;
import com.eldercare.exception.custom.ResourceNotFoundException;
import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.resident_intake.pre_admission.PreAdmissionScreeningEntity;
import com.eldercare.modules.resident_intake.pre_admission.dto.request.PreCreateRequest;
import com.eldercare.modules.resident_intake.pre_admission.dto.request.PreDecisionRequest;
import com.eldercare.modules.resident_intake.pre_admission.dto.response.PreResponse;
import com.eldercare.modules.resident_intake.pre_admission.dto.response.PreSelectDTO;
import com.eldercare.modules.resident_intake.pre_admission.mapper.PreAdmissionScreeningMapper;
import com.eldercare.modules.resident_intake.pre_admission.repository.PreAdmissionScreeningRepository;
import com.eldercare.modules.resident_intake.pre_admission.service.PreAdmissionScreeningService;
import com.eldercare.modules.resident_intake.resident.repository.ResidentRepository;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreAdmissionScreeningServiceImpl implements PreAdmissionScreeningService {
  private final PreAdmissionScreeningRepository preRepo;
  private final ResidentRepository residentRepo;
  private final PreAdmissionScreeningMapper mapper;

  @Override
  @Transactional
  public PreResponse create(PreCreateRequest req) {
    ResidentEntity resident = residentRepo.findById(req.getResidentId())
        .orElseThrow(() -> new ResourceNotFoundException("Resident not found"));

    // Deactivate previous non-draft screening, block if a draft is still open
    preRepo.findByResidentIdAndIsCurrentTrue(resident.getId())
        .ifPresent(old -> {
          if ("DRAFT".equals(old.getStatus())) {
            throw new ConflictException("Resident already has a draft pre-admission screening in progress");
          }
          old.setIsCurrent(false);
          preRepo.save(old);
        });

    PreAdmissionScreeningEntity pre = new PreAdmissionScreeningEntity();
    pre.setResident(resident);
    pre.setStatus("DRAFT");
    pre.setIsCurrent(true);
    UserEntity u = new UserEntity();
    // TODO: replace hardcoded id with SecurityUtils.getCurrentUserId()
    u.setId(1L);
    pre.setScreenedBy(u);
    pre.setCreatedAt(OffsetDateTime.now());
    return mapper.toResponse(preRepo.save(pre));
  }

  @Override
  public Page<PreResponse> listPaged(Pageable pageable) {
    return preRepo.findAll(pageable).map(mapper::toResponse);
  }

  @Override
  public List<PreSelectDTO> listCompletedForSelect() {
    return preRepo.findByStatusAndIsCurrentTrue("COMPLETED").stream()
        .map(mapper::toSelectDTO)
        .toList();
  }

  @Override
  @Transactional
  public PreResponse decide(Long id, PreDecisionRequest req) {
    PreAdmissionScreeningEntity pre = preRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    // Only COMPLETED or REJECTED are valid decision outcomes
    if (!req.getStatus().equals("COMPLETED") && !req.getStatus().equals("REJECTED"))
      throw new BadRequestException("Invalid status");
    pre.setStatus(req.getStatus());
    return mapper.toResponse(preRepo.save(pre));
  }

  @Override
  @Transactional
  public void deleteDraft(Long id) {
    PreAdmissionScreeningEntity pre = preRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    // Guard: only DRAFT records can be removed
    if (!"DRAFT".equals(pre.getStatus()))
      throw new BadRequestException("Only DRAFT can be deleted");
    preRepo.delete(pre);
  }
}
