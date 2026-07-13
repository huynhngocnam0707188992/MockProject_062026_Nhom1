import {
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export const CareTaskTableHeader = () => {
  return (
    <TableHeader>
      <TableRow className="bg-muted/30 hover:bg-muted/30 border-b border-border">
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground w-[22%]">
          Resident
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground w-[14%]">
          Task Type
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground w-[22%]">
          Goal
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground text-center w-[10%]">
          Time
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground text-center w-[10%]">
          Status
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground text-center w-[8%]">
          Flags
        </TableHead>
        <TableHead className="px-4 py-3 text-xs font-semibold uppercase tracking-wider text-muted-foreground text-right w-[14%]">
          Actions
        </TableHead>
      </TableRow>
    </TableHeader>
  );
};
