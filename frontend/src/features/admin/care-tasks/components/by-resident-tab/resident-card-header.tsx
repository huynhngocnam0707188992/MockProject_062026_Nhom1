import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { User, History } from "lucide-react";
import type { ResidentGroup } from "@/services/care-tasks-api";

interface ResidentCardHeaderProps {
  resident: ResidentGroup;
}

// SVG circle: r=14 → circumference = 2π×14 ≈ 87.96
const CIRCUMFERENCE = 2 * Math.PI * 14;

export const ResidentCardHeader = ({ resident }: ResidentCardHeaderProps) => {
  // Dot colours
  const dotColor =
    resident.statusDot === "alert"
      ? "bg-red-500"
      : resident.statusDot === "fall-risk"
      ? "bg-yellow-400"
      : "bg-emerald-500";

  // Task calculations
  const remaining = resident.totalTasks - resident.completedTasks;
  const progressRatio = resident.totalTasks > 0 ? resident.completedTasks / resident.totalTasks : 0;
  const strokeDashoffset = CIRCUMFERENCE * (1 - progressRatio);
  const hasMissed = typeof resident.missedTasks === "number" && resident.missedTasks > 0;

  return (
    <div className="flex items-center justify-between px-5 py-3.5 bg-muted/30 border-b border-border">
      {/* Left — Avatar + details */}
      <div className="flex items-center gap-4">
        <div className="relative">
          <Avatar size="lg" className="ring-2 ring-background shadow-sm flex-shrink-0 size-12">
            <AvatarImage src={resident.imageUrl} alt={resident.name} />
            <AvatarFallback className="bg-primary/10 text-primary font-semibold">
              {resident.name
                .split(" ")
                .map((n) => n[0])
                .join("")
                .slice(0, 2)}
            </AvatarFallback>
          </Avatar>
          {/* Status Dot */}
          <div
            className={`absolute bottom-0 right-0 size-3.5 rounded-full ring-2 ring-background ${dotColor}`}
            title={resident.careLevel}
          />
        </div>

        <div className="flex flex-col gap-1">
          <div className="flex items-center gap-2">
            <h2 className="text-[15px] font-semibold text-foreground leading-none">
              {resident.name}
            </h2>
            <Badge variant="secondary" className="px-1.5 py-0 h-[18px] text-[10px] font-medium bg-muted text-muted-foreground border-transparent">
              {resident.age} yrs
            </Badge>
          </div>

          <div className="flex items-center text-[11px] text-muted-foreground gap-2 font-medium">
            <span className="flex items-center gap-1">
              Room {resident.room}
            </span>
            <span className="size-1 rounded-full bg-border" />
            <span>{resident.careLevel}</span>
          </div>
        </div>
      </div>

      {/* Right — progress ring + task counts + actions */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-3 rounded-lg border border-border bg-card px-3.5 py-2 shadow-sm">
          {/* Crisp SVG progress ring */}
          <div className="relative size-10 flex-shrink-0">
            <svg className="size-full -rotate-90" viewBox="0 0 36 36" fill="none">
              {/* Track */}
              <circle
                cx="18"
                cy="18"
                r="14"
                stroke="currentColor"
                strokeWidth="3"
                className="text-border"
              />
              {/* Progress */}
              <circle
                cx="18"
                cy="18"
                r="14"
                stroke="currentColor"
                strokeWidth="3"
                strokeLinecap="round"
                strokeDasharray={CIRCUMFERENCE}
                strokeDashoffset={strokeDashoffset}
                className={hasMissed ? "text-destructive" : progressRatio === 1 ? "text-emerald-500" : "text-primary"}
                style={{ transition: "stroke-dashoffset 0.4s ease" }}
              />
            </svg>
            {/* Counter label centred inside ring */}
            <span className="absolute inset-0 flex items-center justify-center text-[10px] font-bold text-foreground tabular-nums">
              {resident.completedTasks}/{resident.totalTasks}
            </span>
          </div>

          {/* Text summary */}
          <div className="flex flex-col gap-0.5 min-w-[90px]">
            <span className="text-sm font-semibold text-foreground leading-tight">
              {resident.totalTasks} Tasks Today
            </span>
            {hasMissed ? (
              <span className="text-[11px] font-medium text-destructive leading-tight">
                {resident.missedTasks} missed · {remaining} remaining
              </span>
            ) : (
              <span className="text-[11px] text-muted-foreground leading-tight">
                {remaining === 0
                  ? "All tasks complete"
                  : `${remaining} remaining`}
              </span>
            )}
          </div>
        </div>
        
        {/* Actions */}
        <div className="flex flex-col gap-1 sm:flex-row sm:gap-1.5 ml-2 border-l border-border pl-4">
          <Button variant="ghost" size="icon" className="size-8 text-muted-foreground hover:text-foreground">
            <User className="size-4" />
          </Button>
          <Button variant="ghost" size="icon" className="size-8 text-muted-foreground hover:text-foreground">
            <History className="size-4" />
          </Button>
        </div>
      </div>
    </div>
  );
};
