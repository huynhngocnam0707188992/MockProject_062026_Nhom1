import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { careTasksApi } from "@/services/care-tasks-api";
import type { CareTaskSearchParams } from "@/services/care-tasks-api";

export function useCareTasks(params?: CareTaskSearchParams) {
  const queryClient = useQueryClient();

  const { data: cnaGroupsData, isLoading: isLoadingCnaGroups, error: errorCnaGroups } = useQuery({
    queryKey: ["care-tasks-by-cna", params],
    queryFn: () => careTasksApi.getTasksByCna(params),
  });

  const { data: residentGroupsData, isLoading: isLoadingResidentGroups, error: errorResidentGroups } = useQuery({
    queryKey: ["care-tasks-by-resident", params],
    queryFn: () => careTasksApi.getTasksByResident(params),
  });

  const completeTaskMutation = useMutation({
    mutationFn: (taskId: string | number) => careTasksApi.completeTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-cna"] });
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-resident"] });
    },
  });

  const rescheduleTaskMutation = useMutation({
    mutationFn: (taskId: string | number) => careTasksApi.rescheduleTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-cna"] });
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-resident"] });
    },
  });

  return {
    cnaGroupsData,
    isLoading: isLoadingCnaGroups,
    residentGroupsData,
    isLoadingResidents: isLoadingResidentGroups,
    error: errorCnaGroups?.message || errorResidentGroups?.message || null,
    completeTask: completeTaskMutation.mutateAsync,
    rescheduleTask: rescheduleTaskMutation.mutateAsync,
  };
}
