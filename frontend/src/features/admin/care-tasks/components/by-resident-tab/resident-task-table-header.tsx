import {
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export const ResidentTaskTableHeader = () => {
  return (
    <TableHeader>
      <TableRow className="bg-muted/40 hover:bg-muted/40 border-b border-border">
        {/* Time — 110px */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground w-[110px]">
          Time
        </TableHead>
        {/* Task Type — 130px */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground w-[130px]">
          Task Type
        </TableHead>
        {/* Goal / Details — auto */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground">
          Goal / Details
        </TableHead>
        {/* Assigned CNA — 200px */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground w-[200px]">
          Assigned CNA
        </TableHead>
        {/* Status — 110px */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground text-center w-[110px]">
          Status
        </TableHead>
        {/* Actions — 70px */}
        <TableHead className="h-10 px-4 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground text-right w-[70px]">
          Actions
        </TableHead>
      </TableRow>
    </TableHeader>
  );
};
