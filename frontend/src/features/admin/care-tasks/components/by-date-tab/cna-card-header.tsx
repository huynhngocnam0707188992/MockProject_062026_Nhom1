import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";

interface CnaCardHeaderProps {
  name: string;
  role: string;
  imageUrl: string;
  imageAlt: string;
  totalTasks: number;
  completedTasks: number;
  missedTasks?: number;
}

export const CnaCardHeader = ({
  name,
  role,
  imageUrl,
  imageAlt,
  totalTasks,
  completedTasks,
  missedTasks,
}: CnaCardHeaderProps) => {
  const remaining = totalTasks - completedTasks;
  const progressPct = totalTasks > 0 ? (completedTasks / totalTasks) * 100 : 0;

  return (
    <div className="flex items-center justify-between px-6 py-4 bg-muted/40 border-b border-border">
      {/* Left: Avatar + Name + Role */}
      <div className="flex items-center gap-3">
        <Avatar size="lg" className="ring-2 ring-background shadow-sm">
          <AvatarImage src={imageUrl} alt={imageAlt} />
          <AvatarFallback className="bg-primary/10 text-primary font-semibold">
            {name
              .split(" ")
              .map((n) => n[0])
              .join("")
              .slice(0, 2)}
          </AvatarFallback>
        </Avatar>

        <div className="flex flex-col">
          <span className="text-sm font-semibold text-foreground leading-tight">
            {name}
          </span>
          <span className="text-xs text-muted-foreground uppercase tracking-wide mt-0.5">
            {role}
          </span>
        </div>
      </div>

      {/* Right: Task summary */}
      <div className="flex items-center gap-3 bg-card rounded-lg px-4 py-2 border border-border shadow-sm">
        {/* Circular progress */}
        <div className="relative size-9 flex items-center justify-center flex-shrink-0">
          <svg className="size-full -rotate-90" viewBox="0 0 36 36">
            <path
              className="text-border"
              d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              fill="none"
              stroke="currentColor"
              strokeWidth="3"
            />
            <path
              className="text-primary"
              d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              fill="none"
              stroke="currentColor"
              strokeDasharray={`${progressPct}, 100`}
              strokeLinecap="round"
              strokeWidth="3"
            />
          </svg>
          <span className="absolute text-[9px] font-bold text-foreground">
            {completedTasks}/{totalTasks}
          </span>
        </div>

        {/* Task count text */}
        <div className="flex flex-col">
          <span className="text-sm font-semibold text-foreground leading-tight">
            {totalTasks} Tasks Today
          </span>
          {missedTasks && missedTasks > 0 ? (
            <Badge variant="destructive" className="mt-0.5 w-fit text-[11px] h-auto py-0.5">
              {missedTasks} Missed
            </Badge>
          ) : (
            <span className="text-xs text-muted-foreground mt-0.5">
              {remaining} remaining
            </span>
          )}
        </div>
      </div>
    </div>
  );
};
