import { apiClient } from "@/lib/api-client";
import type { ApiResponse, PagedApiResponse as PagedResponse } from "@/types/api";

export interface EnrichedTaskRow {
  id: number;
  careInterventionId: number;
  carePlanId: number;
  residentId: number;
  residentDisplayName: string;
  roomNumber: string;
  assignedCnaId: number | null;
  assignedCnaDisplayName: string | null;
  scheduledTime: string;
  completedAt: string | null;
  taskType: string;
  status: string;
  isAbnormalFlagged: boolean;
  goal: string;
}

export interface GroupedByCnaCard {
  cnaId: number | null;
  cnaDisplayName: string | null;
  totalTasks: number;
  completedTasks: number;
  missedTasks: number;
  tasks: EnrichedTaskRow[];
}

export interface GroupedByResidentCard {
  residentId: number;
  residentDisplayName: string;
  roomNumber: string;
  carePlanId: number;
  totalTasks: number;
  completedTasks: number;
  missedTasks: number;
  tasks: EnrichedTaskRow[];
}

export interface CareTaskSearchParams {
  date?: string;
  status?: string;
  taskType?: string;
  residentId?: number;
  assignedCnaId?: number;
  isAbnormalFlagged?: boolean;
  fromDate?: string;
  toDate?: string;
  page?: number;
  size?: number;
}

export const careTasksApi = {
  getTasksByCna: async (params?: CareTaskSearchParams): Promise<PagedResponse<GroupedByCnaCard[]>> => {
    const { data } = await apiClient.get<ApiResponse<PagedResponse<GroupedByCnaCard[]>>>("/tasks/by-cna", { params });
    return data.data;
  },

  getTasksByResident: async (params?: CareTaskSearchParams): Promise<PagedResponse<GroupedByResidentCard[]>> => {
    const { data } = await apiClient.get<ApiResponse<PagedResponse<GroupedByResidentCard[]>>>("/tasks/by-resident", { params });
    return data.data;
  },

  searchTasks: async (params?: CareTaskSearchParams): Promise<PagedResponse<EnrichedTaskRow[]>> => {
    const { data } = await apiClient.get<ApiResponse<PagedResponse<EnrichedTaskRow[]>>>("/tasks/search", { params });
    return data.data;
  },

  completeTask: async (taskId: string | number, completedAt?: string): Promise<void> => {
    await apiClient.patch(`/tasks/${taskId}/completed`, { completedAt });
  },

  rescheduleTask: async (taskId: string | number, scheduledTime: string): Promise<void> => {
    await apiClient.patch(`/tasks/${taskId}/reschedule`, { scheduledTime });
  },

  markMissed: async (taskId: string | number): Promise<void> => {
    await apiClient.patch(`/tasks/${taskId}/missed`);
  },

  flagAbnormal: async (taskId: string | number, isAbnormalFlagged: boolean): Promise<void> => {
    await apiClient.patch(`/tasks/${taskId}/flag-abnormal`, { isAbnormalFlagged });
  },

  assignCna: async (taskId: string | number, assignedCnaId: number | null): Promise<void> => {
    await apiClient.patch(`/tasks/${taskId}/assign-cna`, { assignedCnaId });
  },

  deleteTask: async (taskId: string | number): Promise<void> => {
    await apiClient.delete(`/tasks/${taskId}`);
  },

  updateTask: async (
    taskId: string | number,
    payload: { taskType?: string; assignedCnaId?: number | null; scheduledTime?: string }
  ): Promise<void> => {
    await apiClient.put(`/tasks/${taskId}`, payload);
  },
};

