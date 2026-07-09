// LOC Rate Types
export interface CareLevelRateResponse {
  id: number;
  careLevelId: number;
  facilityId: number;
  dailyRate: number;
  effectiveFrom: string;
  effectiveTo: string | null;
}

export interface CreateCareLevelRateRequest {
  careLevelId: number;
  facilityId: number;
  dailyRate: number;
  effectiveFrom: string;
}

export interface UpdateCareLevelRateRequest {
  careLevelId?: number;
  facilityId?: number;
  dailyRate?: number;
  effectiveFrom?: string;
  effectiveTo?: string | null;
}

export interface CareLevelResponse {
  id: number;
  levelCode: string;
  levelName: string;
  isDeleted: boolean;
}

export interface UpdateCareLevelRequest {
  isDeleted: boolean;
}

// Care Plan Types
export interface CarePlan {
  id: number;
  residentId: number;
  careLevelId: number;
  status: 'ACTIVE' | 'INACTIVE' | 'COMPLETED';
  startDate: string;
  endDate: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface Goal {
  id: number;
  carePlanId: number;
  description: string;
  targetDate: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'ACHIEVED' | 'NOT_ACHIEVED';
  progress: number;
}

export interface Intervention {
  id: number;
  goalId: number;
  description: string;
  frequency: string;
  notes: string;
}