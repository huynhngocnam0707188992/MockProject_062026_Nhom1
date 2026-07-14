package com.eldercare.modules.resident_intake.assessment.mapper;

import org.springframework.stereotype.Component;

import com.eldercare.modules.resident_intake.assessment.AssessmentEntity;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentResponse;
import com.eldercare.modules.resident_intake.assessment.dto.response.AssessmentSelectDTO;
import com.eldercare.modules.resident_intake.assessment_detail.dto.response.AssessmentDetailResponse;
import com.eldercare.modules.resident_intake.care_level.CareLevelEntity;

@Component
public class AssessmentMapper {
  public AssessmentResponse toResponse(AssessmentEntity a) {
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

  public AssessmentSelectDTO toSelectDTO(AssessmentEntity a) {
    AssessmentSelectDTO dto = new AssessmentSelectDTO();
    dto.setId(a.getId());
    dto.setResidentName(a.getResident().getFirstName() + " " + a.getResident().getLastName());
    dto.setAdlTotalScore(a.getAdlTotalScore());
    return dto;
  }

  public CareLevelEntity calculateSuggestedCareLevel(int totalScore) {
    CareLevelEntity cl = new CareLevelEntity();
    cl.setId(totalScore >= 80 ? 1L : totalScore >= 50 ? 2L : 3L);
    return cl;
  }
}
