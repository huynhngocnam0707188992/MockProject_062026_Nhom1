import { axiosInstance } from "@/lib/axios";
import type { ApiResponse } from "@/types/api";
import type { FacilitySelectResponse } from "../types/facility-type";

const BASE_URL = "/admin/facility-settings";

// GET /api/v1/admin/facility-settings/select
export const getFacilityForSelect = async () => {
  const { data } = await axiosInstance.get<
    ApiResponse<FacilitySelectResponse[]>
  >(`${BASE_URL}/select`);
  return data;
};
