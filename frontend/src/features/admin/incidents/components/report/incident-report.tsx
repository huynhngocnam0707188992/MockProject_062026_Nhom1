"use client"

import { IncidentDetailsSection } from "./incident-details-section";
import { ReportingPartySidebar } from "./reporting-party-sidebar";
import { ReportFooter } from "./report-footer";

export function IncidentReport() {
  return (
    <div className="space-y-6">
      <div className="rounded-[20px] border border-border bg-background p-6 shadow-sm">
        <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-muted-foreground">
              Report New Incident
            </p>
            <h1 className="mt-2 text-2xl font-semibold text-foreground">
              Incident Report
            </h1>
          </div>
          <p className="max-w-2xl text-sm leading-6 text-muted-foreground">
            Complete all required fields. Submitting will auto-lock the resident&apos;s chart (BR-07).
          </p>
        </div>
        <div className="grid gap-6 xl:grid-cols-[1.4fr_0.8fr]">
          <IncidentDetailsSection />
          <ReportingPartySidebar />
        </div>
      </div>
      <ReportFooter />
    </div>
  );
}
