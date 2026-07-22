import apiClient from "@/common/config/api";
import type {
    ShiftRequirement,
    StaffingRatioConfig,
    UpdateStaffingRatioPayload,
} from "../types/staffing-ratio.type";

interface ShiftRequirementApiResponse {
    id: number | null;
    shift_id: number;
    shift_name: string;
    start_time: string;
    end_time: string;
    required_cna_hours: number;
    required_nurse_hours: number;
    subtotal: number;
}

interface StaffingRatioApiResponse {
    id: number;
    facility_id: number;
    min_hrs_per_resident_day: number;
    warn_below_percentage: number;
    shift_requirements: ShiftRequirementApiResponse[];
    total_cna_hours: number;
    total_nurse_hours: number;
    sum_of_shifts: number;
}

const mapShiftRequirement = (
    item: ShiftRequirementApiResponse,
): ShiftRequirement => ({
    id: item.id,
    shiftId: item.shift_id,
    shiftName: item.shift_name,
    startTime: item.start_time,
    endTime: item.end_time,
    requiredCnaHours: item.required_cna_hours,
    requiredNurseHours: item.required_nurse_hours,
    subtotal: item.subtotal,
});

const mapStaffingRatio = (
    data: StaffingRatioApiResponse,
): StaffingRatioConfig => ({
    id: data.id,
    facilityId: data.facility_id,
    minHrsPerResidentDay: data.min_hrs_per_resident_day,
    warnBelowPercentage: data.warn_below_percentage,
    shiftRequirements: data.shift_requirements.map(mapShiftRequirement),
    totalCnaHours: data.total_cna_hours,
    totalNurseHours: data.total_nurse_hours,
    sumOfShifts: data.sum_of_shifts,
});

export const getStaffingRatio = async (): Promise<StaffingRatioConfig> => {
    const response = await apiClient.get<StaffingRatioApiResponse>(
        "/admin/staffing-ratio-config",
    );
    return mapStaffingRatio(response.data);
};

export const updateStaffingRatio = async (
    payload: UpdateStaffingRatioPayload,
): Promise<StaffingRatioConfig> => {
    const response = await apiClient.put<StaffingRatioApiResponse>(
        "/admin/staffing-ratio-config",
        {
            min_hrs_per_resident_day: payload.minHrsPerResidentDay,
            warn_below_percentage: payload.warnBelowPercentage,
            shift_requirements: payload.shiftRequirements.map((item) => ({
                shift_id: item.shiftId,
                required_cna_hours: item.requiredCnaHours,
                required_nurse_hours: item.requiredNurseHours,
            })),
        },
    );
    return mapStaffingRatio(response.data);
};
