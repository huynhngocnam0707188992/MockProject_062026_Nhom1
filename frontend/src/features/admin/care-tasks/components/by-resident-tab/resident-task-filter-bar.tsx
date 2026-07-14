import { CalendarDays, Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Separator } from "@/components/ui/separator";

export const ResidentTaskFilterBar = () => {
  return (
    <div className="rounded-xl border bg-card shadow-sm">
      <div className="flex flex-wrap items-center gap-2 px-4 py-3">
        {/* Date Picker */}
        <Button
          variant="outline"
          className="h-8 gap-2 px-3 text-sm font-medium border-border bg-background hover:bg-muted"
        >
          <CalendarDays className="size-3.5 text-muted-foreground" />
          Oct 24, 2026
        </Button>

        <Separator orientation="vertical" className="h-5 mx-1" />

        {/* Status */}
        <div className="flex items-center gap-1.5">
          <span className="text-xs font-medium text-muted-foreground whitespace-nowrap">
            Status
          </span>
          <Select defaultValue="all-status">
            <SelectTrigger className="h-8 w-28 text-xs border-border bg-background">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-status">All Statuses</SelectItem>
              <SelectItem value="pending">Pending</SelectItem>
              <SelectItem value="in-progress">In Progress</SelectItem>
              <SelectItem value="completed">Completed</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Flag */}
        <div className="flex items-center gap-1.5">
          <span className="text-xs font-medium text-muted-foreground whitespace-nowrap">
            Flag
          </span>
          <Select defaultValue="all-flags">
            <SelectTrigger className="h-8 w-28 text-xs border-border bg-background">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-flags">All</SelectItem>
              <SelectItem value="flagged">Flagged</SelectItem>
              <SelectItem value="unflagged">Unflagged</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Push search to the right */}
        <div className="flex-1" />

        {/* Search Tasks */}
        <div className="relative min-w-[200px]">
          <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground pointer-events-none" />
          <Input
            className="pl-8 h-8 text-sm border-border bg-background"
            placeholder="Search task or resident..."
            type="text"
          />
        </div>
      </div>
    </div>
  );
};
