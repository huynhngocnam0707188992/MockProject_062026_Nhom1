export interface ShiftRequirement {
    id: number | null;
    shiftId: number;
    shiftName: string;
    startTime: string;
    endTime: string;
    requiredCnaHours: number;
    requiredNurseHours: number;
    subtotal: number;
}

export interface StaffingRatioConfig {
    id: number;
    facilityId: number;
    minHrsPerResidentDay: number;
    warnBelowPercentage: number;
    shiftRequirements: ShiftRequirement[];
    totalCnaHours: number;
    totalNurseHours: number;
    sumOfShifts: number;
}

export interface UpdateStaffingRatioPayload {
    minHrsPerResidentDay: number;
    warnBelowPercentage: number;
    shiftRequirements: Array<{
        shiftId: number;
        requiredCnaHours: number;
        requiredNurseHours: number;
    }>;
}
