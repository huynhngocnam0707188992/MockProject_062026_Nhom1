import { axiosInstance } from "@/lib/axios";
import type { ApiResponse, PagedApiResponse } from "@/types/api";
import type {
  AdmissionCreateRequest,
  AdmissionDischargeRequest,
  AdmissionResponse,
} from "../types/admission-type";

const BASE_URL = "/admissions";

export interface AdmissionListParams {
  page: number;
  size: number;
  sort: string;
}

// GET /api/v1/admissions
export const getAdmissions = async (params: AdmissionListParams) => {
  const { data } = await axiosInstance.get<
    PagedApiResponse<AdmissionResponse[]>
  >(BASE_URL, {
    params,
  });

  return data;
};

// POST /api/v1/admissions
export const createAdmission = async (payload: AdmissionCreateRequest) => {
  const { data } = await axiosInstance.post<ApiResponse<AdmissionResponse>>(
    BASE_URL,
    payload,
  );

  return data;
};

// PUT /api/v1/admissions/{id}/discharge
export const dischargeAdmission = async (
  id: number,
  payload: AdmissionDischargeRequest,
) => {
  const { data } = await axiosInstance.put<ApiResponse<AdmissionResponse>>(
    `${BASE_URL}/${id}/discharge`,
    payload,
  );

  return data;
};
