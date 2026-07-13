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

export const CareTaskFilterBar = () => {
  return (
    <div className="rounded-xl border bg-card shadow-sm p-4">
      <div className="flex flex-wrap items-center gap-3">
        {/* Date Picker */}
        <Button variant="outline" className="flex items-center gap-2 h-9 px-3">
          <CalendarDays className="size-4 text-muted-foreground" />
          <span className="text-sm font-medium">Oct 24, 2026</span>
        </Button>

        <div className="w-px h-6 bg-border" />

        {/* Shift Select */}
        <Select defaultValue="all-shifts">
          <SelectTrigger className="h-9 w-36">
            <span className="text-xs text-muted-foreground mr-1">Shift:</span>
            <SelectValue placeholder="All" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all-shifts">All</SelectItem>
            <SelectItem value="morning">Morning</SelectItem>
            <SelectItem value="afternoon">Afternoon</SelectItem>
            <SelectItem value="night">Night</SelectItem>
          </SelectContent>
        </Select>

        {/* Status Select */}
        <Select defaultValue="all-status">
          <SelectTrigger className="h-9 w-40">
            <span className="text-xs text-muted-foreground mr-1">Status:</span>
            <SelectValue placeholder="All" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all-status">All</SelectItem>
            <SelectItem value="pending">Pending</SelectItem>
            <SelectItem value="done">Completed</SelectItem>
            <SelectItem value="missed">Missed</SelectItem>
          </SelectContent>
        </Select>

        {/* CNA Select */}
        <Select defaultValue="all-cnas">
          <SelectTrigger className="h-9 w-40">
            <span className="text-xs text-muted-foreground mr-1">CNA:</span>
            <SelectValue placeholder="All CNAs" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all-cnas">All CNAs</SelectItem>
            <SelectItem value="sarah">Sarah G.</SelectItem>
            <SelectItem value="mark">Mark J.</SelectItem>
          </SelectContent>
        </Select>

        {/* Task Type Select */}
        <Select defaultValue="all-types">
          <SelectTrigger className="h-9 w-40">
            <span className="text-xs text-muted-foreground mr-1">Type:</span>
            <SelectValue placeholder="All Types" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all-types">All Types</SelectItem>
            <SelectItem value="bathing">Bathing</SelectItem>
            <SelectItem value="medication">Medication</SelectItem>
            <SelectItem value="meals">Meals</SelectItem>
          </SelectContent>
        </Select>

        {/* Spacer */}
        <div className="flex-1 min-w-[80px]" />

        {/* Resident Search */}
        <div className="relative">
          <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-4 text-muted-foreground pointer-events-none" />
          <Input
            className="pl-9 h-9 w-52"
            placeholder="Search resident..."
            type="text"
          />
        </div>
      </div>
    </div>
  );
};
