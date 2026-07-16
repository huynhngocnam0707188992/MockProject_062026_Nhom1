"use client";

import * as React from "react";

import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { incidentSeverityApi, type SeverityLevel } from "@/services/incidents/incidents-severity-api";

const inputClass =
  "h-10 w-full rounded-md border border-gray-300 bg-white px-3 text-sm focus:border-primary focus:ring-2 focus:ring-primary/20";
const inputStyles =
  "h-10 w-full rounded-lg border border-input bg-transparent px-3 text-sm outline-none transition-colors placeholder:text-muted-foreground focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50 disabled:cursor-not-allowed disabled:opacity-50";

function Textarea(props: React.TextareaHTMLAttributes<HTMLTextAreaElement>) {
  return (
    <textarea
      {...props}
      className={inputStyles + " min-h-[104px] resize-none py-2"}
    />
  );
}

function Field({
  label,
  required,
  children,
}: {
  label: string;
  required?: boolean;
  children: React.ReactNode;
}) {
  return (
    <div className="space-y-2">
      <Label className="text-sm font-medium text-foreground">
        {label}
        {required && <span className="ml-1 text-destructive">*</span>}
      </Label>
      {children}
    </div>
  );
}

// incidentType values must match the backend enum exactly (IncidentType.java):
// FALL, MEDICATION_ERROR, ALTERCATION, SKIN_TEAR
const incidentTypeOptions: { value: string; label: string }[] = [
  { value: "FALL", label: "Fall" },
  { value: "MEDICATION_ERROR", label: "Medication Error" },
  { value: "ALTERCATION", label: "Altercation" },
  { value: "SKIN_TEAR", label: "Skin Tear" },
];

export type IncidentFormValues = {
  residentId?: number;
  residentLabel: string;
  incidentType: string;
  severityId?: number;
  dateTime: string;
  location: string;
  description: string;
  witnesses: string;
  immediateAction: string;
};

export type IncidentDetailsData = {
  residentId?: number;
  resident?: string;
  incidentType?: string;
  severityId?: number;
  dateTime?: string;
  location?: string;
  description?: string;
  witnesses?: string;
  immediateAction?: string;
};

export function IncidentDetailsSection({
  mode = "edit",
  data,
  onChange,
}: {
  mode?: "edit" | "view";
  data?: IncidentDetailsData;
  // Called on every field change so the parent (incident-report.tsx) can
  // hold the current form state and use it when the user hits "Report Incident".
  onChange?: (values: IncidentFormValues) => void;
}) {
  const [residentLabel, setResidentLabel] = React.useState(data?.resident ?? "Robert Hayes - Room 204B");
  const [residentId, setResidentId] = React.useState<number | undefined>(data?.residentId);
  const [incidentType, setIncidentType] = React.useState(data?.incidentType ?? "FALL");
  const [severityId, setSeverityId] = React.useState<number | undefined>(data?.severityId);
  const [dateTime, setDateTime] = React.useState(data?.dateTime ?? "2026-07-03T09:15");
  const [location, setLocation] = React.useState(data?.location ?? "Room 204B - bathroom");
  const [description, setDescription] = React.useState(
    data?.description ??
      "Resident found on bathroom floor near the toilet; complaint of right hip pain. No loss of consciousness observed."
  );
  const [witnesses, setWitnesses] = React.useState(data?.witnesses ?? "Marcus Rivera, CNA (present at time of fall)");
  const [immediateAction, setImmediateAction] = React.useState(
    data?.immediateAction ?? "Assisted resident to bed, vitals taken, physician notified per facility protocol."
  );

  const [severities, setSeverities] = React.useState<SeverityLevel[]>([]);
  const [severitiesError, setSeveritiesError] = React.useState<string | null>(null);

  React.useEffect(() => {
    incidentSeverityApi
      .getAll()
      .then((list) => {
        setSeverities(list);
        // default to first option if nothing selected yet
        if (severityId === undefined && list.length > 0) {
          setSeverityId(list[0].id);
        }
      })
      .catch((e) => setSeveritiesError(e?.response?.data?.message ?? e.message));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Push current values up to parent whenever anything changes.
  React.useEffect(() => {
    onChange?.({
      residentId,
      residentLabel,
      incidentType,
      severityId,
      dateTime,
      location,
      description,
      witnesses,
      immediateAction,
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [residentId, residentLabel, incidentType, severityId, dateTime, location, description, witnesses, immediateAction]);

  return (
    <div className="rounded-[20px] border border-border bg-card p-6 shadow-sm">
      <div className="mb-6 flex items-center justify-between gap-4">
        <div>
          <h2 className="text-lg font-semibold text-foreground">Incident Details</h2>
          <p className="text-sm text-muted-foreground">
            Provide the incident information below so the clinical team can review and act promptly.
          </p>
        </div>
      </div>

      <div className="space-y-5">
        <div className="grid gap-4 md:grid-cols-2">
          <Field label="Resident" required>
            {/* TODO: replace with a real resident picker (dropdown/search) that
                sets residentId to the resident's actual DB id. Free text here
                cannot be sent as residentID to POST /api/v1/incidents. */}
            <Input
              className={inputClass}
              value={residentLabel}
              onChange={(e) => setResidentLabel(e.target.value)}
              placeholder="Chọn resident..."
            />
          </Field>

          <Field label="Incident Type" required>
            <select
              className={inputClass}
              value={incidentType}
              onChange={(e) => setIncidentType(e.target.value)}
            >
              {incidentTypeOptions.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
          </Field>
        </div>

        <div className="grid gap-4 md:grid-cols-2">
          <Field label="Severity" required>
            <select
              className={inputClass}
              value={severityId ?? ""}
              onChange={(e) => setSeverityId(e.target.value ? Number(e.target.value) : undefined)}
              disabled={severities.length === 0}
            >
              {severities.length === 0 && <option value="">Đang tải...</option>}
              {severities.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.level_name}
                </option>
              ))}
            </select>
            {severitiesError && (
              <p className="text-xs text-destructive">Không tải được danh sách severity: {severitiesError}</p>
            )}
          </Field>

          <Field label="Date / Time of Incident" required>
            <Input
              type="datetime-local"
              className={inputClass}
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
            />
          </Field>
        </div>

        <Field label="Location" required>
          <Input
            className={inputClass}
            value={location}
            onChange={(e) => setLocation(e.target.value)}
          />
        </Field>

        <Field label="Description" required>
          <Textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Field>

        <Field label="Immediate Action Taken" required>
          <Textarea
            value={immediateAction}
            onChange={(e) => setImmediateAction(e.target.value)}
          />
        </Field>

        <Field label="Witnesses (optional)">
          <Textarea
            value={witnesses}
            onChange={(e) => setWitnesses(e.target.value)}
          />
        </Field>

        <div className="flex items-center justify-end gap-3 pt-2">
          <Button variant="outline">Cancel</Button>
          {/* Actual "Save"/"Report Incident" submit button lives in report-footer.tsx,
              which reads the values from onChange above via the parent state. */}
        </div>
      </div>
    </div>
  );
}