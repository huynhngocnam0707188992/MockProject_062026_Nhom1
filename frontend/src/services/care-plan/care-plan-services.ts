import { apiClient } from "@/lib/api-client";
import type { GetCarePlanListResponse } from "./care-plan-types";
import type { PagedApiResponse } from "@/types/api";

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
  const response = await apiClient.get<PagedApiResponse<GetCarePlanListResponse>>("/care-plans", {
    params,
  });

  // Handle both the old format (directly in response.data) and the new ApiResponse wrapped format
  const responseData: any = response.data.data ? response.data.data : response.data;
  
  if (Array.isArray(responseData)) {
      return responseData;
  }
  
  if (responseData.list) {
      return responseData.list;
  }
  
  if (responseData.content) {
      return responseData.content;
  }
  
  if (responseData.data && Array.isArray(responseData.data)) {
      return responseData.data;
  }
  
  if (responseData.data && responseData.data.list) {
      return responseData.data.list;
  }

  return [];
};
