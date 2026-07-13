import { AlertTriangle, CheckCircle2, MoreVertical, Clock } from "lucide-react";
import type { ResidentTask } from "@/services/care-tasks-api";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { TableCell, TableRow } from "@/components/ui/table";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

interface ResidentTaskRowProps {
  task: ResidentTask;
}

export const ResidentTaskRow = ({ task }: ResidentTaskRowProps) => {
  const isCompleted = task.status === "Done";
  const isMissed = task.status === "Missed";

  // ── Status badge ─────────────────────────────────────────────────────────
  const statusBadge = () => {
    if (isCompleted) {
      return (
        <Badge className="gap-1 border-transparent bg-emerald-50 text-emerald-700 dark:bg-emerald-900/25 dark:text-emerald-400 font-medium text-[11px] px-2 py-0.5 h-auto whitespace-nowrap">
          <CheckCircle2 className="size-3 shrink-0" />
          Done
        </Badge>
      );
    }
    if (isMissed) {
      return (
        <Badge className="gap-1 border-transparent bg-red-50 text-red-600 dark:bg-red-900/25 dark:text-red-400 font-medium text-[11px] px-2 py-0.5 h-auto whitespace-nowrap">
          <AlertTriangle className="size-3 shrink-0" />
          Missed
        </Badge>
      );
    }
    return (
      <Badge className="gap-1 border-transparent bg-amber-50 text-amber-700 dark:bg-amber-900/25 dark:text-amber-400 font-medium text-[11px] px-2 py-0.5 h-auto whitespace-nowrap">
        <Clock className="size-3 shrink-0" />
        Pending
      </Badge>
    );
  };

  const rowBg = isMissed
    ? "bg-red-50/40 hover:bg-red-50/70 dark:bg-red-950/10 dark:hover:bg-red-950/20"
    : "hover:bg-muted/40";

  return (
    <TableRow
      className={`group transition-colors border-b border-border/60 last:border-b-0 ${rowBg}`}
    >
      {/* ── Time ────────────────────────────────────────── w-[110px] */}
      <TableCell className="px-4 py-3 w-[110px]">
        <span className="text-[12px] font-semibold text-foreground">
          {task.time}
        </span>
      </TableCell>

      {/* ── Task Type ───────────────────────────────────── w-[130px] */}
      <TableCell className="px-4 py-3 w-[130px]">
        <Badge
          className={`text-[11px] font-medium px-2 py-0.5 h-auto border-transparent whitespace-nowrap ${
            isMissed
              ? "bg-muted text-muted-foreground"
              : isCompleted
              ? "bg-blue-50 text-blue-700 dark:bg-blue-900/25 dark:text-blue-400"
              : "bg-violet-50 text-violet-700 dark:bg-violet-900/25 dark:text-violet-400"
          }`}
        >
          {task.taskType}
        </Badge>
      </TableCell>

      {/* ── Goal / Details ──────────────────────────────── auto width */}
      <TableCell className="px-4 py-3">
        <div className="flex flex-col gap-0.5 max-w-sm">
          <div className="flex items-center gap-1.5">
            <span className="text-xs font-semibold text-foreground">
              {task.goalTitle}
            </span>
            {task.hasFlag && (
              <span
                className="inline-flex items-center justify-center size-4 rounded bg-red-100 text-red-500 dark:bg-red-900/30"
                title={task.flagNote || "Attention required"}
              >
                <AlertTriangle className="size-2.5" />
              </span>
            )}
          </div>
          <span className="text-[11px] text-muted-foreground line-clamp-1 leading-snug">
            {task.goalDetail}
          </span>
        </div>
      </TableCell>

      {/* ── Assigned CNA ────────────────────────────────── w-[200px] */}
      <TableCell className="px-4 py-3 w-[200px]">
        <div className="flex items-center gap-2">
          {task.assignedCnaImageUrl ? (
            <Avatar className="size-6 flex-shrink-0">
              <AvatarImage src={task.assignedCnaImageUrl} alt={task.assignedCnaName} />
              <AvatarFallback className="bg-primary/10 text-primary text-[10px] font-semibold">
                {task.assignedCnaName.charAt(0)}
              </AvatarFallback>
            </Avatar>
          ) : (
            <div className="size-6 rounded-full bg-muted flex items-center justify-center text-[10px] font-bold text-muted-foreground">
              Un
            </div>
          )}
          <span className={`text-[12px] truncate ${!task.assignedCnaImageUrl ? "text-muted-foreground italic" : "text-foreground font-medium"}`}>
            {task.assignedCnaName}
          </span>
        </div>
      </TableCell>

      {/* ── Status ──────────────────────────────────────── w-[110px] */}
      <TableCell className="px-4 py-3 w-[110px] text-center">
        <div className="flex items-center justify-center">
          {statusBadge()}
        </div>
      </TableCell>

      {/* ── Actions ─────────────────────────────────────── w-[70px] */}
      <TableCell className="px-4 py-3 w-[70px]">
        <div className="flex items-center justify-end">
          <DropdownMenu>
            <DropdownMenuTrigger
              className="inline-flex size-7 items-center justify-center rounded-md text-muted-foreground hover:bg-muted hover:text-foreground transition-colors outline-none focus-visible:ring-2 focus-visible:ring-ring/50"
              aria-label="More options"
            >
              <MoreVertical className="size-3.5" />
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="min-w-[150px]">
              {!isCompleted && <DropdownMenuItem>Complete Task</DropdownMenuItem>}
              <DropdownMenuItem>View Details</DropdownMenuItem>
              <DropdownMenuItem>Reassign</DropdownMenuItem>
              {!isMissed && !isCompleted && (
                <DropdownMenuItem variant="destructive">
                  Mark as Missed
                </DropdownMenuItem>
              )}
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </TableCell>
    </TableRow>
  );
};
