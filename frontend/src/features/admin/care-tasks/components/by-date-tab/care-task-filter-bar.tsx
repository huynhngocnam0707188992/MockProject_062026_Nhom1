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

export const CareTaskFilterBar = () => {
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

        {/* Shift */}
        <div className="flex items-center gap-1.5">
          <span className="text-xs font-medium text-muted-foreground whitespace-nowrap">
            Shift
          </span>
          <Select defaultValue="all-shifts">
            <SelectTrigger className="h-8 w-28 text-xs border-border bg-background">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-shifts">All</SelectItem>
              <SelectItem value="morning">Morning</SelectItem>
              <SelectItem value="afternoon">Afternoon</SelectItem>
              <SelectItem value="night">Night</SelectItem>
            </SelectContent>
          </Select>
        </div>

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
              <SelectItem value="all-status">All</SelectItem>
              <SelectItem value="pending">Pending</SelectItem>
              <SelectItem value="done">Completed</SelectItem>
              <SelectItem value="missed">Missed</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* CNA */}
        <div className="flex items-center gap-1.5">
          <span className="text-xs font-medium text-muted-foreground whitespace-nowrap">
            CNA
          </span>
          <Select defaultValue="all-cnas">
            <SelectTrigger className="h-8 w-32 text-xs border-border bg-background">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-cnas">All CNAs</SelectItem>
              <SelectItem value="sarah">Sarah G.</SelectItem>
              <SelectItem value="mark">Mark J.</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Task Type */}
        <div className="flex items-center gap-1.5">
          <span className="text-xs font-medium text-muted-foreground whitespace-nowrap">
            Type
          </span>
          <Select defaultValue="all-types">
            <SelectTrigger className="h-8 w-32 text-xs border-border bg-background">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-types">All Types</SelectItem>
              <SelectItem value="bathing">Bathing</SelectItem>
              <SelectItem value="medication">Medication</SelectItem>
              <SelectItem value="meals">Meals</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Push search to the right */}
        <div className="flex-1" />

        {/* Resident Search */}
        <div className="relative min-w-[200px]">
          <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground pointer-events-none" />
          <Input
            className="pl-8 h-8 text-sm border-border bg-background"
            placeholder="Search resident…"
            type="text"
          />
        </div>

      </div>
    </div>
  );
};
