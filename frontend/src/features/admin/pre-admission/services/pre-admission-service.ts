import { axiosInstance } from "@/lib/axios";
import type { ApiResponse, PagedApiResponse } from "@/types/api";
import type {
  PreCreateRequest,
  PreDecisionRequest,
  PreResponse,
  PreSelectDTO,
} from "../types/pre-admission-type";

const BASE_URL = "/pre-admissions";

export interface ScreeningListParams {
  page: number;
  size: number;
  sort: string;
}

// GET /api/v1/pre-admissions
export const getScreenings = async (params: ScreeningListParams) => {
  const { data } = await axiosInstance.get<PagedApiResponse<PreResponse[]>>(
    BASE_URL,
    {
      params,
    },
  );

  return data;
};

// GET /api/v1/pre-admissions/select-completed
export const getCompletedScreenings = async () => {
  const { data } = await axiosInstance.get<ApiResponse<PreSelectDTO[]>>(
    `${BASE_URL}/select-completed`,
  );

  return data;
};

// POST /api/v1/pre-admissions
export const createScreening = async (payload: PreCreateRequest) => {
  const { data } = await axiosInstance.post<ApiResponse<PreResponse>>(
    BASE_URL,
    payload,
  );

  return data;
};

// PUT /api/v1/pre-admissions/{id}/decision
export const decideScreening = async (
  id: number,
  payload: PreDecisionRequest,
) => {
  const { data } = await axiosInstance.put<ApiResponse<PreResponse>>(
    `${BASE_URL}/${id}/decision`,
    payload,
  );

  return data;
};

// DELETE /api/v1/pre-admissions/{id}
export const deleteScreening = async (id: number) => {
  const { data } = await axiosInstance.delete<ApiResponse<null>>(
    `${BASE_URL}/${id}`,
  );

  return data;
};
