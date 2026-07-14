import { useCareTasks } from "../hooks/useCareTasks";
import { ResidentCardHeader } from "../components/by-resident-tab/resident-card-header";
import { ResidentTaskTableHeader } from "../components/by-resident-tab/resident-task-table-header";
import { ResidentTaskRow } from "../components/by-resident-tab/resident-task-row";
import { ResidentTaskFilterBar } from "../components/by-resident-tab/resident-task-filter-bar";
import { Table, TableBody } from "@/components/ui/table";
import { ClipboardList } from "lucide-react";

export const ByResidentTab = () => {
  const { residentGroups, isLoadingResidents } = useCareTasks();

  if (isLoadingResidents) {
    return (
      <div className="flex items-center justify-center py-20 text-muted-foreground text-sm gap-2">
        <div className="size-4 border-2 border-muted-foreground/30 border-t-muted-foreground rounded-full animate-spin" />
        Loading care tasks…
      </div>
    );
  }

  if (residentGroups.length === 0) {
    return (
      <div className="mt-5 flex flex-col items-center justify-center gap-3 py-20 rounded-xl border bg-card text-center">
        <ClipboardList className="size-10 text-muted-foreground/40" />
        <p className="text-sm text-muted-foreground">
          No residents with care tasks found.
        </p>
      </div>
    );
  }

  return (
    <div className="flex flex-col w-full gap-5 mt-5">
      {/* Filter bar */}
      <ResidentTaskFilterBar />

      {/* One card per Resident */}
      {residentGroups.map((resident) => (
        <div
          key={resident.id}
          className="rounded-xl border border-border bg-card shadow-sm overflow-hidden"
        >
          {/* Card header — Resident identity + status */}
          <ResidentCardHeader resident={resident} />

          {/* Table */}
          <div className="overflow-x-auto">
            <Table className="table-fixed min-w-[720px]">
              <ResidentTaskTableHeader />
              <TableBody>
                {resident.tasks.map((task) => (
                  <ResidentTaskRow key={task.id} task={task} />
                ))}
              </TableBody>
            </Table>
          </div>
        </div>
      ))}
    </div>
  );
};
