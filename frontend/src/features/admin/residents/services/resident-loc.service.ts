import { axiosInstance } from "@/lib/axios";
import type { CareLevelHistory } from "../types/loc.type";

export const residentLocService = {

    getHistory: async (
        residentId: number
    ): Promise<CareLevelHistory[]> => {

        const response = await axiosInstance.get(
            `/api/v1/residents/${residentId}/care-level-history`
        );

        return response.data;
    }

};