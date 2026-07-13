import { useCareTasks } from "../hooks/useCareTasks";
import { CnaCardHeader } from "../components/by-date-tab/cna-card-header";
import { CareTaskTableHeader } from "../components/by-date-tab/care-task-table-header";
import { CareTaskRow } from "../components/by-date-tab/care-task-row";
import { CareTaskFilterBar } from "../components/by-date-tab/care-task-filter-bar";
import { Table, TableBody } from "@/components/ui/table";

export const ByDateTab = () => {
  const { cnaGroups, isLoading } = useCareTasks();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-16 text-muted-foreground text-sm">
        Loading care tasks...
      </div>
    );
  }

  return (
    <div className="flex flex-col w-full gap-6 pb-6 mt-4">
      <CareTaskFilterBar />

      {cnaGroups.map((cnaGroup) => (
        <div
          key={cnaGroup.id}
          className="rounded-xl border bg-card shadow-sm overflow-hidden"
        >
          {/* CNA Card Header */}
          <CnaCardHeader
            name={cnaGroup.name}
            role={cnaGroup.role}
            imageUrl={cnaGroup.imageUrl}
            imageAlt={cnaGroup.imageAlt}
            totalTasks={cnaGroup.totalTasks}
            completedTasks={cnaGroup.completedTasks}
            missedTasks={cnaGroup.missedTasks}
          />

          {/* Task Table — Table component already provides overflow-x-auto */}
          <Table>
            <CareTaskTableHeader />
            <TableBody>
              {cnaGroup.tasks.map((task) => (
                <CareTaskRow key={task.id} task={task} />
              ))}
            </TableBody>
          </Table>
        </div>
      ))}

      {cnaGroups.length === 0 && (
        <div className="rounded-xl border bg-card shadow-sm p-12 text-center text-muted-foreground text-sm">
          No care tasks found for the selected date.
        </div>
      )}
    </div>
  );
};
