package com.eldercare.modules.resident_intake.assessment.service.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eldercare.exception.ConflictException;
import com.eldercare.exception.custom.BadRequestException;
import com.eldercare.exception.custom.ResourceNotFoundException;
import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.resident_intake.assessment.AssessmentEntity;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentCreateRequest;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentDecisionRequest;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentUpdateRequest;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentResponse;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentSelectDTO;
import com.eldercare.modules.resident_intake.assessment.mapper.AssessmentMapper;
import com.eldercare.modules.resident_intake.assessment.repository.AssessmentRepository;
import com.eldercare.modules.resident_intake.assessment.service.AssessmentService;
import com.eldercare.modules.resident_intake.assessment_detail.AssessmentDetailEntity;
import com.eldercare.modules.resident_intake.assessment_detail.dto.request.AssessmentDetailRequest;
import com.eldercare.modules.resident_intake.assessment_metric.AssessmentMetricEntity;
import com.eldercare.modules.resident_intake.assessment_metric.dto.AssessmentMetricDTO;
import com.eldercare.modules.resident_intake.assessment_metric.repository.AssessmentMetricRepository;
import com.eldercare.modules.resident_intake.care_level.CareLevelEntity;
import com.eldercare.modules.resident_intake.pre_admission.PreAdmissionScreeningEntity;
import com.eldercare.modules.resident_intake.pre_admission.repository.PreAdmissionScreeningRepository;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

  private final AssessmentRepository assessRepo;
  private final AssessmentMetricRepository metricRepo;
  private final PreAdmissionScreeningRepository preRepo;
  private final AssessmentMapper mapper;

  @Override
  public List<AssessmentMetricDTO> getMetrics() {
    return metricRepo.findAll().stream().map(m -> {
      AssessmentMetricDTO dto = new AssessmentMetricDTO();
      dto.setId(m.getId());
      dto.setCategory(m.getCategory());
      dto.setMetricName(m.getMetricName());
      return dto;
    }).toList();
  }

  @Override
  @Transactional
  public AssessmentResponse create(AssessmentCreateRequest req) {
    PreAdmissionScreeningEntity pre = preRepo.findById(req.getPreAdmissionScreeningId())
        .orElseThrow(() -> new ResourceNotFoundException("PAS not found"));

    // Assessment can only be created from a completed, current pre-admission
    // screening
    if (!"COMPLETED".equals(pre.getStatus()) || !Boolean.TRUE.equals(pre.getIsCurrent()))
      throw new BadRequestException("Pre-admission screening is not valid");

    ResidentEntity resident = pre.getResident();

    // Deactivate previous non-draft assessment, block if a draft is still open
    assessRepo.findByResidentIdAndIsCurrentTrue(resident.getId())
        .ifPresent(old -> {
          if ("DRAFT".equals(old.getStatus())) {
            throw new ConflictException("Resident already has a draft assessment in progress");
          }
          old.setIsCurrent(false);
          assessRepo.save(old);
        });

    AssessmentEntity a = new AssessmentEntity();
    a.setResident(resident);
    a.setPreAdmissionScreening(pre);
    a.setStatus("DRAFT");
    a.setIsCurrent(true);
    a.setIsOverridden(false);

    UserEntity u = new UserEntity();
    // TODO: replace hardcoded id with SecurityUtils.getCurrentUserId()
    u.setId(1L);
    a.setAssessedBy(u);
    a.setCreatedAt(OffsetDateTime.now());

    // Build details and accumulate ADL total score
    int total = 0;
    List<AssessmentDetailEntity> details = new ArrayList<>();

    for (AssessmentDetailRequest d : req.getDetails()) {
      AssessmentDetailEntity det = new AssessmentDetailEntity();

      AssessmentMetricEntity metric = new AssessmentMetricEntity();
      metric.setId(d.getMetricId());

      det.setMetric(metric);
      det.setScore(d.getScore());
      det.setNotes(d.getNotes());
      det.setAssessment(a);

      details.add(det);
      total += d.getScore();
    }

    a.setDetails(details);
    a.setAdlTotalScore(total);
    a.setSuggestedCareLevel(mapper.calculateSuggestedCareLevel(total));

    return mapper.toResponse(assessRepo.save(a));
  }

  @Override
  @Transactional
  public AssessmentResponse update(Long id, AssessmentUpdateRequest req) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));

    // Only editable while still in DRAFT
    if (!"DRAFT".equals(a.getStatus()))
      throw new BadRequestException("Only DRAFT assessment can be updated");

    int total = 0;
    List<AssessmentDetailEntity> details = new ArrayList<>();
    for (AssessmentDetailRequest d : req.getDetails()) {
      AssessmentDetailEntity det = new AssessmentDetailEntity();
      AssessmentMetricEntity metric = new AssessmentMetricEntity();
      metric.setId(d.getMetricId());
      det.setMetric(metric);
      det.setScore(d.getScore());
      det.setNotes(d.getNotes());
      det.setAssessment(a);
      details.add(det);
      total += d.getScore();
    }

    // Replace old details in place to keep the managed collection reference
    a.getDetails().clear();
    a.getDetails().addAll(details);
    a.setAdlTotalScore(total);
    a.setSuggestedCareLevel(mapper.calculateSuggestedCareLevel(total));

    return mapper.toResponse(assessRepo.save(a));
  }

  @Override
  public Page<AssessmentResponse> listPaged(Pageable pageable) {
    return assessRepo.findAll(pageable).map(mapper::toResponse);
  }

  @Override
  @Transactional
  public AssessmentResponse decide(Long id, AssessmentDecisionRequest req) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));

    a.setStatus(req.getStatus());

    if (req.getConfirmedCareLevelId() != null) {
      CareLevelEntity cl = new CareLevelEntity();
      cl.setId(req.getConfirmedCareLevelId());
      a.setConfirmedCareLevel(cl);

      Long suggestedId = a.getSuggestedCareLevel() != null
          ? a.getSuggestedCareLevel().getId()
          : null;

      // Flag override when the confirmed level differs from the system-suggested one
      a.setIsOverridden(
          suggestedId != null
              && !suggestedId.equals(req.getConfirmedCareLevelId()));
    }

    return mapper.toResponse(assessRepo.save(a));
  }

  @Override
  public List<AssessmentSelectDTO> listCompletedForSelect() {
    return assessRepo.findByStatusAndIsCurrentTrue("COMPLETED").stream()
        .map(mapper::toSelectDTO)
        .toList();
  }

  @Override
  @Transactional
  public void deleteDraft(Long id) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    // Guard: only DRAFT records can be removed
    if (!"DRAFT".equals(a.getStatus()))
      throw new BadRequestException("Only DRAFT can be deleted");
    assessRepo.delete(a);
  }
}