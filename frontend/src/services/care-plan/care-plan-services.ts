import { apiClient } from "@/lib/api-client";
import type {
  GetCarePlanDetailResponse,
  GetCarePlanListResponse,
} from "./care-plan-types";

export type GetCarePlanListParams = {
  page?: number;
  size?: number;
  residentId?: number;
  status?: string;
  significantChangeFlag?: boolean;
  sortBy?: string;
  sortDir?: "ASC" | "DESC";
};

export const getCarePlanList = async (
  params?: GetCarePlanListParams,
): Promise<GetCarePlanListResponse> => {
  const response = await apiClient.get<GetCarePlanListResponse>("/care-plans", {
    params,
  });

  return response.data;
};

export const getCarePlanDetail = async (id: number) => {
  const response = await apiClient.get<GetCarePlanDetailResponse>(
    `/care-plans/${id}`,
  );

  return response.data;
};
