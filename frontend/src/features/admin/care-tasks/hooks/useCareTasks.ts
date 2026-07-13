import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { careTasksApi } from "@/services/care-tasks-api";

export function useCareTasks(date?: string) {
  const queryClient = useQueryClient();

  const { data: cnaGroups, isLoading: isLoadingCnaGroups, error: errorCnaGroups } = useQuery({
    queryKey: ["care-tasks-by-date", date],
    queryFn: () => careTasksApi.getTasksByDate(date),
  });

  const { data: residentGroups, isLoading: isLoadingResidentGroups, error: errorResidentGroups } = useQuery({
    queryKey: ["care-tasks-by-resident", date],
    queryFn: () => careTasksApi.getTasksByResident(date),
  });

  const completeTaskMutation = useMutation({
    mutationFn: (taskId: string) => careTasksApi.completeTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-date"] });
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-resident"] });
    },
  });

  const rescheduleTaskMutation = useMutation({
    mutationFn: (taskId: string) => careTasksApi.rescheduleTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-date"] });
      queryClient.invalidateQueries({ queryKey: ["care-tasks-by-resident"] });
    },
  });

  return {
    cnaGroups: cnaGroups || [],
    isLoading: isLoadingCnaGroups,
    residentGroups: residentGroups || [],
    isLoadingResidents: isLoadingResidentGroups,
    error: errorCnaGroups?.message || errorResidentGroups?.message || null,
    completeTask: completeTaskMutation.mutateAsync,
    rescheduleTask: rescheduleTaskMutation.mutateAsync,
  };
}
