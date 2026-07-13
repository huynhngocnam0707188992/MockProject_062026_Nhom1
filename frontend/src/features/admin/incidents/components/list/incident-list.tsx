import { Button } from "@/components/ui/button";
import { AlarmClock, Clock, FileCheck2, Layers } from "lucide-react";
import { CardSummary, FilterDropdown, IncidentsTable, Pagination } from ".";

const cards = [
  { label: "Open", value: 3, icon: <AlarmClock className="size-5 text-amber-500" />, iconBg: "bg-amber-100" },
  { label: "Overdue (SLA)", value: 1, icon: <Clock className="size-5 text-red-500" />, iconBg: "bg-red-100" },
  { label: "Chart Locked", value: 2, icon: <Layers className="size-5 text-pink-500" />, iconBg: "bg-pink-100" },
  { label: "Resolved (month)", value: 9, icon: <FileCheck2 className="size-5 text-emerald-600" />, iconBg: "bg-emerald-100" },
];

const incidents = [
  {
    id: 1,
    resident: "Susan Wright · 114B",
    type: "Elopement Risk",
    severity: "Critical",
    reported: "2026-07-02 22:10",
    sla: "OVERDUE",
    slaStyle: "text-red-600",
    status: "Open",
    chart: "Locked",
  },
  {
    id: 2,
    resident: "Robert Hayes · 204B",
    type: "Fall",
    severity: "Major",
    reported: "2026-07-03 09:15",
    sla: "14h left",
    slaStyle: "text-red-600",
    status: "Open",
    chart: "Locked",
  },
  {
    id: 3,
    resident: "Mary Coleman · 118A",
    type: "Skin Injury",
    severity: "Moderate",
    reported: "2026-07-02 08:00",
    sla: "36h left",
    slaStyle: "text-emerald-600",
    status: "Open",
    chart: "Unlocked",
  },
  {
    id: 4,
    resident: "James Porter · 210B",
    type: "Behavioral",
    severity: "Minor",
    reported: "2026-06-30 14:20",
    sla: "—",
    slaStyle: "text-slate-400",
    status: "Submitted",
    chart: "Unlocked",
  },
  {
    id: 5,
    resident: "David Nguyen · 222A",
    type: "Medication Error",
    severity: "Major",
    reported: "2026-06-28 11:05",
    sla: "—",
    slaStyle: "text-slate-400",
    status: "Resolved",
    chart: "Unlocked",
  },
];

const severityStyles: Record<string, string> = {
  Critical: "bg-red-100 text-red-600",
  Major: "bg-orange-100 text-orange-600",
  Moderate: "bg-amber-100 text-amber-700",
  Minor: "bg-slate-100 text-slate-600",
};

const statusStyles: Record<string, string> = {
  Open: "bg-amber-100 text-amber-700",
  Submitted: "bg-sky-100 text-sky-700",
  Resolved: "bg-emerald-100 text-emerald-700",
};

const chartStyles: Record<string, string> = {
  Locked: "bg-red-100 text-red-600",
  Unlocked: "bg-emerald-100 text-emerald-700",
};

export function IncidentList() {
  return (
    <div className="space-y-6">
      <div>
        <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-slate-900">Incidents</h1>
            <p className="mt-1 text-sm text-slate-500">12 incidents this month · 3 open</p>
          </div>
          <Button className="rounded-full bg-blue-600 px-5 py-3 text-white hover:bg-blue-700">+ Report Incident</Button>
        </div>

        <div className="mt-4 flex flex-wrap gap-3">
          <FilterDropdown label="Status: All" />
          <FilterDropdown label="Severity: All" />
        </div>
      </div>

      <CardSummary cards={cards} />

      <IncidentsTable incidents={incidents} severityStyles={severityStyles} statusStyles={statusStyles} chartStyles={chartStyles}>
        <Pagination />
      </IncidentsTable>
    </div>
  );
}
