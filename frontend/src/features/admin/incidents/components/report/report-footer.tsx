"use client"

import { Button } from "@/components/ui/button";

export function ReportFooter() {
  return (
    <div className="flex flex-col gap-3 rounded-[20px] border border-border bg-card p-6 shadow-sm sm:flex-row sm:items-center sm:justify-end">
      <div className="flex flex-1 items-center text-sm text-muted-foreground">
        <p>Ready to submit? Review details before reporting.</p>
      </div>
      <div className="flex flex-wrap gap-3">
        <Button variant="outline">Cancel</Button>
        <Button>Report Incident</Button>
      </div>
    </div>
  );
}
