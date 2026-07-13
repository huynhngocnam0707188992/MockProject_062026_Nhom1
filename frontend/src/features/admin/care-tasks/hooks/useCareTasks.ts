import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { careTasksApi } from "@/services/care-tasks-api";

export function useCareTasks(date?: string) {
  const queryClient = useQueryClient();

  const { data, isLoading, error } = useQuery({
    queryKey: ["care-tasks", date],
    queryFn: () => careTasksApi.getTasksByDate(date),
  });

  const completeTaskMutation = useMutation({
    mutationFn: (taskId: string) => careTasksApi.completeTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks"] });
    },
  });

  const rescheduleTaskMutation = useMutation({
    mutationFn: (taskId: string) => careTasksApi.rescheduleTask(taskId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["care-tasks"] });
    },
  });

  return {
    cnaGroups: data || [],
    isLoading,
    error: error?.message || null,
    completeTask: completeTaskMutation.mutateAsync,
    rescheduleTask: rescheduleTaskMutation.mutateAsync,
  };
}
