export type AdmissionStatus = "ACTIVE" | "DISCHARGED";

export interface AdmissionCreateRequest {
  assessmentId: number;
  facilityId: number;
  admissionDate: string;
}

export interface AdmissionDischargeRequest {
  dischargeDate: string;
  dischargeReason: string;
}

export interface AdmissionResponse {
  id: number;
  admissionDate: string;
  residentId: number;
  residentName: string;
  facilityId: number;
  assessmentId: number;
  dischargeDate: string | null;
  dischargeReason: string | null;
  status: AdmissionStatus;
}
