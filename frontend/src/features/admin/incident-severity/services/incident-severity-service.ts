import apiClient from "@/common/config/api";

export type IncidentSeverityResponse = {
  id: number;
  levelName: string;
  chartLockTrigger: boolean;
};

type IncidentSeverityApiResponse = {
  id: number;
  level_name: string;
  chart_lock_trigger: boolean;
};

export type CreateIncidentSeverityRequest = {
  level_name: string;
  chart_lock_trigger: boolean;
};

export type UpdateIncidentSeverityRequest = {
  level_name: string;
  chart_lock_trigger: boolean;
  description?: string;
  example?: string;
};

export const createIncidentSeverityLevel = async (
  request: CreateIncidentSeverityRequest
): Promise<IncidentSeverityResponse> => {
  const response = await apiClient.post<IncidentSeverityApiResponse>(
    "/admin/incident-severity-levels",
    request
  );

  return {
    id: response.data.id,
    levelName: response.data.level_name,
    chartLockTrigger: response.data.chart_lock_trigger,
  };
};

export const fetchIncidentSeverityLevels = async (): Promise<IncidentSeverityResponse[]> => {
  const response = await apiClient.get<IncidentSeverityApiResponse[]>("/admin/incident-severity-levels");
  console.log("API response incident severity levels:", response.data);

  return response.data.map((item) => ({
    id: item.id,
    levelName: item.level_name,
    chartLockTrigger: item.chart_lock_trigger,
  }));
};

export const updateIncidentSeverityLevel = async (
  severityId: number,
  request: UpdateIncidentSeverityRequest
): Promise<IncidentSeverityResponse> => {
  const response = await apiClient.put<IncidentSeverityApiResponse>(
    `/admin/incident-severity-levels/${severityId}`,
    request
  );

  return {
    id: response.data.id,
    levelName: response.data.level_name,
    chartLockTrigger: response.data.chart_lock_trigger,
  };
};
