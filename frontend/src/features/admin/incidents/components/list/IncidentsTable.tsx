import React, { useState } from "react";
import { X } from "lucide-react";
import {
  IncidentDetailPage,
  type IncidentDetailsData,
} from "@/features/admin/incidents/components/report/incident-detail-view";
import { ChartLockedModal } from "@/features/admin/incidents/components/report/chart-locked-modal";

type Incident = {
  id: number;
  resident: string;
  type: string;
  severity: string;
  reported: string;
  sla: string;
  slaStyle: string;
  status: string;
  chart: string;
};

// Extra mock data for rows that don't carry the full narrative in the table itself.
// Keyed by incident id — add an entry here as real detail data becomes available per row.
const extraDetails: Record<number, Partial<IncidentDetailsData>> = {
  2: {
    reportedBy: "Anna Lee, RN",
    location: "Room 204B — bathroom",
    description: "Resident found on bathroom floor near the toilet; c/o right hip pain.",
    witnesses: "Marcus Rivera, CNA (present at time of fall)",
    immediateAction: "Assisted resident to bed, vitals taken, physician notified.",
    chartSince: "09:22",
    slaDeadline: "2026-07-04 09:15",
    slaRule: "NFR-06 · 24-48h regulatory window",
    attachments: [{ name: "incident_form_signed.pdf" }, { name: "photo_bruise.jpg" }],
    timeline: [
      { title: "Chart auto-locked (BR-07)", actor: "System", time: "2026-07-03 09:22", filled: true },
      { title: "DON notified · SLA countdown started", actor: "System", time: "2026-07-03 09:20" },
      { title: "Incident reported", actor: "Anna Lee, RN", time: "2026-07-03 09:15" },
    ],
  },
};

export function IncidentsTable({
  incidents,
  severityStyles,
  statusStyles,
  chartStyles,
  children,
}: {
  incidents: Incident[];
  severityStyles: Record<string, string>;
  statusStyles: Record<string, string>;
  chartStyles: Record<string, string>;
  children?: React.ReactNode;
}) {
  const [selected, setSelected] = useState<Incident | null>(null);
  // When a locked incident is opened, we show the Chart Locked notice first.
  // Clicking "View Incident" inside that notice flips this to true and reveals
  // the normal read-only detail view underneath.
  const [showDetailAnyway, setShowDetailAnyway] = useState(false);

  const open = (incident: Incident) => {
    setSelected(incident);
    setShowDetailAnyway(false);
  };
  const close = () => {
    setSelected(null);
    setShowDetailAnyway(false);
  };

  const isLockedRow = (inc: Incident) => inc.chart === "Locked";

  const dataFrom = (inc: Incident): IncidentDetailsData => {
    const [name, room] = inc.resident.split(" · ");
    return {
      id: `INC-${2000 + inc.id}`,
      resident: name ?? inc.resident,
      room,
      incidentType: inc.type,
      severity: inc.severity,
      status: inc.status,
      reported: inc.reported,
      sla: inc.sla,
      chartStatus: inc.chart as "Locked" | "Unlocked",
      ...extraDetails[inc.id],
    };
  };

  return (
    <>
      <div className="overflow-x-auto rounded-[20px] border border-gray-200 bg-white shadow-sm">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-slate-50">
            <tr>
              {[
                "Resident",
                "Type",
                "Severity",
                "Reported",
                "SLA Countdown ▲",
                "Status",
                "Chart",
                "",
              ].map((heading) => (
                <th
                  key={heading}
                  className="px-6 py-4 text-left text-xs font-semibold uppercase tracking-[0.2em] text-slate-500"
                >
                  {heading}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {incidents.map((incident) => (
              <tr key={incident.id} className="hover:bg-slate-50">
                <td className="px-6 py-5 font-semibold text-slate-900">{incident.resident}</td>
                <td className="px-6 py-5 text-slate-600">{incident.type}</td>
                <td className="px-6 py-5">
                  <span
                    className={
                      "inline-flex rounded-full px-3 py-1 text-xs font-semibold " +
                      severityStyles[incident.severity]
                    }
                  >
                    {incident.severity}
                  </span>
                </td>
                <td className="px-6 py-5 text-slate-600">{incident.reported}</td>
                <td className={"px-6 py-5 font-semibold " + incident.slaStyle}>{incident.sla}</td>
                <td className="px-6 py-5">
                  <span
                    className={
                      "inline-flex rounded-full px-3 py-1 text-xs font-semibold " + statusStyles[incident.status]
                    }
                  >
                    {incident.status}
                  </span>
                </td>
                <td className="px-6 py-5">
                  <span
                    className={
                      "inline-flex rounded-full px-3 py-1 text-xs font-semibold " + chartStyles[incident.chart]
                    }
                  >
                    {incident.chart}
                  </span>
                </td>
                <td className="px-6 py-5 text-right">
                  <button
                    onClick={() => open(incident)}
                    className="text-sm font-semibold text-sky-600 hover:text-sky-800"
                  >
                    View
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {children}
      </div>

      {selected && isLockedRow(selected) && !showDetailAnyway && (
        <ChartLockedModal
          open
          residentName={selected.resident.split(" · ")[0] ?? selected.resident}
          lockedAt={extraDetails[selected.id]?.chartSince ?? selected.reported}
          incidentId={dataFrom(selected).id}
          onBackToProfile={close}
          onViewIncident={() => setShowDetailAnyway(true)}
        />
      )}

      {selected && (!isLockedRow(selected) || showDetailAnyway) && (
        <div
          className="fixed inset-0 z-50 flex items-start justify-center overflow-y-auto bg-black/50 p-6"
          onClick={close}
        >
          <div
            className="relative mx-auto w-full max-w-4xl rounded-lg bg-white p-6 shadow-lg"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              onClick={close}
              aria-label="Close"
              className="absolute right-4 top-4 text-slate-400 hover:text-slate-600"
            >
              <X className="size-5" />
            </button>
            <IncidentDetailPage mode="view" data={dataFrom(selected)} />
          </div>
        </div>
      )}
    </>
  );
}