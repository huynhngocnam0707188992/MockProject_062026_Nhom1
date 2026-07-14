import { useCareTasks } from "../hooks/useCareTasks";
import { CnaCardHeader } from "../components/by-cna-tab/cna-card-header";
import { CareTaskTableHeader } from "../components/by-cna-tab/care-task-table-header";
import { CareTaskRow } from "../components/by-cna-tab/care-task-row";
import { CareTaskFilterBar } from "../components/by-cna-tab/care-task-filter-bar";
import { Table, TableBody } from "@/components/ui/table";
import { ClipboardList } from "lucide-react";

export const ByCnaTab = () => {
  const { cnaGroups, isLoading } = useCareTasks();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20 text-muted-foreground text-sm gap-2">
        <div className="size-4 border-2 border-muted-foreground/30 border-t-muted-foreground rounded-full animate-spin" />
        Loading care tasks…
      </div>
    );
  }

  if (cnaGroups.length === 0) {
    return (
      <div className="mt-5 flex flex-col items-center justify-center gap-3 py-20 rounded-xl border bg-card text-center">
        <ClipboardList className="size-10 text-muted-foreground/40" />
        <p className="text-sm text-muted-foreground">
          No care tasks found for the selected date.
        </p>
      </div>
    );
  }

  return (
    <div className="flex flex-col w-full gap-5 mt-5">
      {/* Filter bar */}
      <CareTaskFilterBar />

      {/* One card per CNA */}
      {cnaGroups.map((cnaGroup) => (
        <div
          key={cnaGroup.id}
          className="rounded-xl border border-border bg-card shadow-sm overflow-hidden"
        >
          {/* Card header — CNA identity + progress */}
          <CnaCardHeader
            name={cnaGroup.name}
            role={cnaGroup.role}
            imageUrl={cnaGroup.imageUrl}
            imageAlt={cnaGroup.imageAlt}
            totalTasks={cnaGroup.totalTasks}
            completedTasks={cnaGroup.completedTasks}
            missedTasks={cnaGroup.missedTasks}
          />

          {/*
            Table with table-fixed so the explicit widths on <th> are
            honoured — this is what makes columns perfectly aligned.
          */}
          <div className="overflow-x-auto">
            <Table className="table-fixed min-w-[720px]">
              <CareTaskTableHeader />
              <TableBody>
                {cnaGroup.tasks.map((task) => (
                  <CareTaskRow key={task.id} task={task} />
                ))}
              </TableBody>
            </Table>
          </div>
        </div>
      ))}
    </div>
  );
};
