import { apiClient } from "@/lib/api-client";
import type { GetCarePlanListResponse } from "./care-plan-types";

export type GetCarePlanListParams = {
  page?: number;
  size?: number;
  residentId?: number;
  status?: string;
  significantChangeFlag?: boolean;
  sortBy?: string;
  sortDir?: "ASC" | "DESC";
};

export const getCarePlanList = async (params?: GetCarePlanListParams) => {
  const response = await apiClient.get<GetCarePlanListResponse>("/care-plans", {
    params,
  });

  return response.data.data.list;
};
