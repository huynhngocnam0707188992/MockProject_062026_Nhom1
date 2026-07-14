import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

interface CnaCardHeaderProps {
  name: string;
  role: string;
  imageUrl: string;
  imageAlt: string;
  totalTasks: number;
  completedTasks: number;
  missedTasks?: number;
}

// SVG circle: r=14 → circumference = 2π×14 ≈ 87.96
const CIRCUMFERENCE = 2 * Math.PI * 14;

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
  const progressRatio = totalTasks > 0 ? completedTasks / totalTasks : 0;
  const strokeDashoffset = CIRCUMFERENCE * (1 - progressRatio);

  const hasMissed = typeof missedTasks === "number" && missedTasks > 0;

  return (
    <div className="flex items-center justify-between px-5 py-3.5 bg-muted/30 border-b border-border">
      {/* Left — Avatar + name + role */}
      <div className="flex items-center gap-3">
        <Avatar size="lg" className="ring-2 ring-background shadow-sm flex-shrink-0">
          <AvatarImage src={imageUrl} alt={imageAlt} />
          <AvatarFallback className="bg-primary/10 text-primary text-sm font-semibold">
            {name
              .split(" ")
              .map((n) => n[0])
              .join("")
              .slice(0, 2)}
          </AvatarFallback>
        </Avatar>

        <div className="flex flex-col gap-0.5">
          <span className="text-sm font-semibold text-foreground leading-none">
            {name}
          </span>
          <span className="text-[11px] font-medium text-muted-foreground uppercase tracking-widest leading-none">
            {role}
          </span>
        </div>
      </div>
    </div>
  );
};
