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
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { useState } from "react";
import { format } from "date-fns";

export const ResidentTaskFilterBar = () => {
  const [date, setDate] = useState<Date | undefined>(new Date("2026-10-24"));

  return (
    <div className="rounded-xl border border-border bg-card shadow-sm">
      <div className="flex flex-wrap items-center gap-3 px-5 py-3.5">
        {/* Date Picker */}
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              className="h-9 gap-2.5 px-4 text-sm font-medium border-border bg-background hover:bg-muted shadow-sm transition-colors"
            >
              <CalendarDays className="size-4 text-muted-foreground" />
              {date ? format(date, "MMM dd, yyyy") : "Pick a date"}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0" align="start">
            <Calendar
              mode="single"
              selected={date}
              onSelect={setDate}
            />
          </PopoverContent>
        </Popover>

        <Separator orientation="vertical" className="h-6 mx-1" />

        {/* Status */}
        <div className="flex items-center gap-2">
          <span className="text-[13px] font-medium text-muted-foreground whitespace-nowrap">
            Status
          </span>
          <Select defaultValue="all-status">
            <SelectTrigger className="h-9 w-32 text-sm border-border bg-background shadow-sm hover:bg-muted/50 transition-colors">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-status">All Statuses</SelectItem>
              <SelectItem value="pending">Pending</SelectItem>
              <SelectItem value="COMPLETED">Completed</SelectItem>
              <SelectItem value="MISSED">Missed</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Task Type */}
        <div className="flex items-center gap-2">
          <span className="text-[13px] font-medium text-muted-foreground whitespace-nowrap">
            Type
          </span>
          <Select defaultValue="all-types">
            <SelectTrigger className="h-9 w-36 text-sm border-border bg-background shadow-sm hover:bg-muted/50 transition-colors">
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

        {/* Resident */}
        <div className="flex items-center gap-2">
          <span className="text-[13px] font-medium text-muted-foreground whitespace-nowrap">
            Resident
          </span>
          <Select defaultValue="all-residents">
            <SelectTrigger className="h-9 w-36 text-sm border-border bg-background shadow-sm hover:bg-muted/50 transition-colors">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all-residents">All Residents</SelectItem>
              <SelectItem value="john">John Doe</SelectItem>
              <SelectItem value="elena">Elena Ramos</SelectItem>
              <SelectItem value="susan">Susan Wright</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Flag */}
        <div className="flex items-center gap-2">
          <span className="text-[13px] font-medium text-muted-foreground whitespace-nowrap">
            Flag
          </span>
          <Select defaultValue="all-flags">
            <SelectTrigger className="h-9 w-32 text-sm border-border bg-background shadow-sm hover:bg-muted/50 transition-colors">
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

        {/* Search Resident */}
        <div className="relative min-w-[240px]">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 size-4 text-muted-foreground pointer-events-none" />
          <Input
            className="pl-9 h-9 text-sm border-border bg-background shadow-sm focus-visible:ring-primary/20"
            placeholder="Search resident..."
            type="text"
          />
        </div>
      </div>
    </div>
  );
};
