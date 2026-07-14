import { axiosInstance } from "@/lib/axios";
import type { CareLevelDTO } from "../types/care-level-type";

// GET /api/v1/admin/care-levels
export const getCareLevels = async () => {
  const { data } =
    await axiosInstance.get<CareLevelDTO[]>("/admin/care-levels");
  return data;
};
