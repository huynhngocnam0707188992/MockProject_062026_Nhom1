export interface AssessmentMetricDTO {
  id: number;
  category: "ADL" | "IADL" | "BRADEN" | "MORSE";
  metricName: string;
}

export interface AssessmentDetailRequest {
  metricId: number;
  score: number;
  notes?: string;
}

export interface AssessmentDetailResponse {
  metricId: number;
  metricName: string;
  category: string;
  score: number;
  notes?: string;
}

export interface AssessmentResponse {
  id: number;
  status: "DRAFT" | "COMPLETED" | "REJECTED";
  adlTotalScore: number;
  residentId: number;
  suggestedCareLevelId: number;
  residentName: string;
  isOverridden: boolean | null;
  details: AssessmentDetailResponse[];
}

export interface AssessmentSelectDTO {
  id: number;
  residentName: string;
  adlTotalScore: number;
}

export interface AssessmentCreateRequest {
  preAdmissionScreeningId: number;
  details: AssessmentDetailRequest[];
}

export interface AssessmentUpdateRequest {
  details: AssessmentDetailRequest[];
}

export interface AssessmentDecisionRequest {
  status: "COMPLETED" | "REJECTED";
  confirmedCareLevelId: number;
}
