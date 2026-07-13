import { AlertTriangle, CheckCircle2, MoreVertical } from "lucide-react";
import type { CareTask } from "@/services/care-tasks-api";
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

interface CareTaskRowProps {
  task: CareTask;
}

export const CareTaskRow = ({ task }: CareTaskRowProps) => {
  const isCompleted = task.status === "Done";
  const isMissed = task.status === "Missed";

  const statusBadge = () => {
    if (isCompleted) {
      return (
        <Badge variant="secondary" className="bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400 border-transparent gap-1">
          <CheckCircle2 className="size-3" />
          Done
        </Badge>
      );
    }
    if (isMissed) {
      return (
        <Badge variant="destructive" className="gap-1">
          Missed
        </Badge>
      );
    }
    return (
      <Badge variant="outline" className="bg-amber-50 text-amber-700 border-amber-200 dark:bg-amber-900/20 dark:text-amber-400 dark:border-amber-800">
        Pending
      </Badge>
    );
  };

  return (
    <TableRow
      className={`
        group relative transition-colors
        ${isMissed ? "bg-destructive/5 hover:bg-destructive/10" : "hover:bg-muted/50"}
      `}
    >
      {/* Left status indicator stripe */}
      <td
        className={`absolute left-0 top-0 bottom-0 w-0.5 ${
          isMissed
            ? "bg-destructive"
            : isCompleted
            ? "bg-emerald-500"
            : "bg-amber-400"
        }`}
        aria-hidden="true"
      />

      {/* Resident */}
      <TableCell className="px-4 py-3">
        <div className="flex items-center gap-3">
          <Avatar className="size-9 flex-shrink-0">
            <AvatarImage
              src={task.residentImageUrl}
              alt={`Portrait of ${task.residentName}`}
            />
            <AvatarFallback className="bg-primary/10 text-primary text-xs font-semibold">
              {task.residentName
                .split(" ")
                .map((n) => n[0])
                .join("")
                .slice(0, 2)}
            </AvatarFallback>
          </Avatar>
          <div className="flex flex-col min-w-0">
            <span className="text-sm font-medium text-foreground truncate leading-tight">
              {task.residentName}
            </span>
            <span className="text-xs text-muted-foreground mt-0.5">
              Room {task.room}
            </span>
          </div>
        </div>
      </TableCell>

      {/* Task Type */}
      <TableCell className="px-4 py-3">
        <Badge
          variant="secondary"
          className={`text-xs gap-1 ${
            isMissed
              ? "bg-muted text-muted-foreground"
              : isCompleted
              ? "bg-primary/10 text-primary"
              : "bg-secondary text-secondary-foreground"
          }`}
        >
          {task.taskType}
        </Badge>
      </TableCell>

      {/* Goal */}
      <TableCell className="px-4 py-3">
        <p className="text-xs text-muted-foreground line-clamp-2 max-w-[200px]">
          {task.goal}
        </p>
      </TableCell>

      {/* Scheduled Time */}
      <TableCell className="px-4 py-3 text-center">
        <span
          className={`inline-block text-xs font-mono font-medium px-2 py-1 rounded ${
            isMissed
              ? "bg-destructive/10 text-destructive"
              : isCompleted
              ? "bg-muted text-muted-foreground"
              : "bg-muted text-foreground"
          }`}
        >
          {task.time}
        </span>
      </TableCell>

      {/* Status */}
      <TableCell className="px-4 py-3 text-center">
        {statusBadge()}
      </TableCell>

      {/* Flags */}
      <TableCell className="px-4 py-3 text-center">
        {task.isAbnormal ? (
          <div
            className="inline-flex items-center justify-center size-7 rounded-full bg-destructive/10 text-destructive animate-pulse"
            title="Abnormal findings flagged"
          >
            <AlertTriangle className="size-3.5" />
          </div>
        ) : (
          <div
            className="inline-flex items-center justify-center size-7 text-muted-foreground/40 group-hover:text-muted-foreground transition-colors"
            title="No abnormal findings"
          >
            <CheckCircle2 className="size-4" />
          </div>
        )}
      </TableCell>

      {/* Actions */}
      <TableCell className="px-4 py-3">
        <div className={`flex items-center justify-end gap-2 ${isCompleted ? "opacity-60" : ""}`}>
          {isCompleted ? (
            <Button
              variant="secondary"
              size="sm"
              disabled
              className="h-7 text-xs"
            >
              Completed
            </Button>
          ) : (
            <>
              <Button
                variant={isMissed ? "outline" : "default"}
                size="sm"
                className="h-7 text-xs"
              >
                {isMissed ? "Reschedule" : "Complete"}
              </Button>
              <DropdownMenu>
                <DropdownMenuTrigger
                  className="inline-flex size-7 items-center justify-center rounded-lg text-muted-foreground hover:bg-muted hover:text-foreground transition-colors outline-none"
                  aria-label="More options"
                >
                  <MoreVertical className="size-3.5" />
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem>View Details</DropdownMenuItem>
                  <DropdownMenuItem>Flag as Abnormal</DropdownMenuItem>
                  <DropdownMenuItem variant="destructive">
                    Mark as Missed
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </>
          )}
        </div>
      </TableCell>
    </TableRow>
  );
};
