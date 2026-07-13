import { useCareTasks } from "../hooks/useCareTasks";
import { CnaCardHeader } from "../components/by-date-tab/cna-card-header";
import { CareTaskTableHeader } from "../components/by-date-tab/care-task-table-header";
import { CareTaskRow } from "../components/by-date-tab/care-task-row";
import { CareTaskFilterBar } from "../components/by-date-tab/care-task-filter-bar";

export const ByDateTab = () => {
  const { cnaGroups, isLoading } = useCareTasks();

  if (isLoading) {
    return (
      <div className="p-8 text-center text-on-surface-variant">Loading care tasks...</div>
    );
  }

  return (
    <div className="flex flex-col w-full gap-stack-lg pb-stack-lg mt-4">
      <CareTaskFilterBar />
      
      {cnaGroups.map((cnaGroup) => (
        <div
          key={cnaGroup.id}
          className="bg-surface-container-lowest rounded-xl shadow-md overflow-hidden flex flex-col"
        >
          <CnaCardHeader
            name={cnaGroup.name}
            role={cnaGroup.role}
            imageUrl={cnaGroup.imageUrl}
            imageAlt={cnaGroup.imageAlt}
            totalTasks={cnaGroup.totalTasks}
            completedTasks={cnaGroup.completedTasks}
            missedTasks={cnaGroup.missedTasks}
          />

          <CareTaskTableHeader />

          <div className="flex flex-col">
            {cnaGroup.tasks.map((task) => (
              <CareTaskRow key={task.id} task={task} />
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};
