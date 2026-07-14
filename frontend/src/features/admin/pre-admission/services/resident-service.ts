import { axiosInstance } from "@/lib/axios";
import type { ApiResponse } from "@/types/api";
import type { ResidentPendingDTO } from "../types/resident-type";

const BASE_URL = "/pre-admissions";

// GET /api/v1/pre-admissions/pending-residents
export const getPendingResidents = async () => {
  const { data } = await axiosInstance.get<ApiResponse<ResidentPendingDTO[]>>(
    `${BASE_URL}/pending-residents`,
  );

  return data;
};
