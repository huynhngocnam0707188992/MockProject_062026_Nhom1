package com.eldercare.modules.resident_intake.assessment.service.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.resident_intake.assessment.AssessmentEntity;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentCreateRequest;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentDecisionRequest;
import com.eldercare.modules.resident_intake.assessment.dto.request.AssessmentUpdateRequest;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentResponse;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentSelectDTO;
import com.eldercare.modules.resident_intake.assessment.repository.AssessmentRepository;
import com.eldercare.modules.resident_intake.assessment.service.AssessmentService;
import com.eldercare.modules.resident_intake.assessment_detail.AssessmentDetailEntity;
import com.eldercare.modules.resident_intake.assessment_detail.dto.request.AssessmentDetailRequest;
import com.eldercare.modules.resident_intake.assessment_detail.dto.response.AssessmentDetailResponse;
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
        .orElseThrow(() -> new RuntimeException("PAS not found"));

    if (!"COMPLETED".equals(pre.getStatus()) || !Boolean.TRUE.equals(pre.getIsCurrent()))
      throw new RuntimeException("Pre-admission screening is not valid");

    ResidentEntity resident = pre.getResident();

    assessRepo.findByResidentIdAndIsCurrentTrue(resident.getId())
        .ifPresent(old -> {
          if ("DRAFT".equals(old.getStatus())) {
            throw new RuntimeException("Resident already has a draft assessment in progress");
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
    // u.setId(SecurityUtils.getCurrentUserId());
    u.setId(1L);
    a.setAssessedBy(u);
    a.setCreatedAt(OffsetDateTime.now());

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
    a.setSuggestedCareLevel(calculateSuggestedCareLevel(total));

    return toResponse(assessRepo.save(a));
  }

  @Override
  @Transactional
  public AssessmentResponse update(Long id, AssessmentUpdateRequest req) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Not found"));

    if (!"DRAFT".equals(a.getStatus()))
      throw new RuntimeException("Only DRAFT assessment can be updated");

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

    a.getDetails().clear();
    a.getDetails().addAll(details);
    a.setAdlTotalScore(total);
    a.setSuggestedCareLevel(calculateSuggestedCareLevel(total));

    return toResponse(assessRepo.save(a));
  }

  @Override
  public Page<AssessmentResponse> listPaged(Pageable pageable) {
    return assessRepo.findAll(pageable)
        .map(this::toResponse);
  }

  @Override
  @Transactional
  public AssessmentResponse decide(Long id, AssessmentDecisionRequest req) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Not found"));

    a.setStatus(req.getStatus());

    if (req.getConfirmedCareLevelId() != null) {
      CareLevelEntity cl = new CareLevelEntity();
      cl.setId(req.getConfirmedCareLevelId());
      a.setConfirmedCareLevel(cl);

      Long suggestedId = a.getSuggestedCareLevel() != null
          ? a.getSuggestedCareLevel().getId()
          : null;

      a.setIsOverridden(
          suggestedId != null
              && !suggestedId.equals(req.getConfirmedCareLevelId()));
    }

    return toResponse(assessRepo.save(a));
  }

  @Override
  public List<AssessmentSelectDTO> listCompletedForSelect() {
    return assessRepo.findByStatusAndIsCurrentTrue("COMPLETED")
        .stream()
        .map(a -> {
          AssessmentSelectDTO dto = new AssessmentSelectDTO();
          dto.setId(a.getId());
          dto.setResidentName(
              a.getResident().getFirstName() + " " + a.getResident().getLastName());
          dto.setAdlTotalScore(a.getAdlTotalScore());
          return dto;
        })
        .toList();
  }

  @Override
  @Transactional
  public void deleteDraft(Long id) {
    AssessmentEntity a = assessRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Not found"));
    if (!"DRAFT".equals(a.getStatus()))
      throw new RuntimeException("Only DRAFT can be deleted");
    assessRepo.delete(a);
  }

  private AssessmentResponse toResponse(AssessmentEntity a) {
    AssessmentResponse dto = new AssessmentResponse();
    dto.setId(a.getId());
    dto.setStatus(a.getStatus());
    dto.setAdlTotalScore(a.getAdlTotalScore());
    dto.setResidentId(a.getResident().getId());
    dto.setSuggestedCareLevelId(a.getSuggestedCareLevel().getId());
    dto.setResidentName(a.getResident().getFirstName() + " " + a.getResident().getLastName());
    dto.setIsOverridden(a.getIsOverridden());
    dto.setDetails(a.getDetails().stream().map(d -> {
      AssessmentDetailResponse r = new AssessmentDetailResponse();
      r.setMetricId(d.getMetric().getId());
      r.setMetricName(d.getMetric().getMetricName());
      r.setCategory(d.getMetric().getCategory());
      r.setScore(d.getScore());
      r.setNotes(d.getNotes());
      return r;
    }).toList());
    return dto;
  }

  private CareLevelEntity calculateSuggestedCareLevel(int totalScore) {
    CareLevelEntity cl = new CareLevelEntity();
    cl.setId(totalScore >= 80 ? 1L : totalScore >= 50 ? 2L : 3L);
    return cl;
  }
}