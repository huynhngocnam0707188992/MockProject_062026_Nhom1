import apiClient from "@/common/config/api";

export type SLAConfigResponse = {
  id: number;
  severityId: number;
  slaWindowHrs: number;
};

type SLAConfigApiResponse = {
  id: number;
  severity_id: number;
  sla_window_hrs: number;
};

export type CreateSLAConfigRequest = {
  severity_id: number;
  sla_window_hrs: number;
};

export type UpdateSLAConfigRequest = {
  severity_id: number;
  sla_window_hrs: number;
  external_report_required?: string;
  regulatory_body?: string;
};

export const createSLAConfig = async (
  request: CreateSLAConfigRequest
): Promise<SLAConfigResponse> => {
  const response = await apiClient.post<SLAConfigApiResponse>(
    "/admin/sla-configs",
    request
  );

  return {
    id: response.data.id,
    severityId: response.data.severity_id,
    slaWindowHrs: response.data.sla_window_hrs,
  };
};

export const fetchSLAConfigs = async (): Promise<SLAConfigResponse[]> => {
  const response = await apiClient.get<SLAConfigApiResponse[]>("/admin/sla-configs");
  console.log("SLA API response:", response.data);
  const mapped = response.data.map((item) => ({
    id: item.id,
    severityId: item.severity_id,
    slaWindowHrs: item.sla_window_hrs,
  }));
  console.log("SLA API mapped response:", mapped);
  return mapped;
};

export const updateSLAConfig = async (
  slaConfigId: number,
  request: UpdateSLAConfigRequest
): Promise<SLAConfigResponse> => {
  const response = await apiClient.put<SLAConfigApiResponse>(
    `/admin/sla-configs/${slaConfigId}`,
    request
  );

  return {
    id: response.data.id,
    severityId: response.data.severity_id,
    slaWindowHrs: response.data.sla_window_hrs,
  };
};
