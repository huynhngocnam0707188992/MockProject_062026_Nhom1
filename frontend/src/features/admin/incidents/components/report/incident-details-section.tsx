"use client";

import * as React from "react";

import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";

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

export type IncidentDetailsData = {
  resident?: string;
  incidentType?: string;
  severity?: string;
  dateTime?: string;
  location?: string;
  description?: string;
  witnesses?: string;
  immediateAction?: string;
};

export function IncidentDetailsSection({
  mode = "edit",
  data,
}: {
  mode?: "edit" | "view";
  data?: IncidentDetailsData;
}) {
  const [resident, setResident] = React.useState(data?.resident ?? "Robert Hayes - Room 204B");
  const [incidentType, setIncidentType] = React.useState(data?.incidentType ?? "Fall");
  const [severity, setSeverity] = React.useState(data?.severity ?? "Major");
  const [dateTime, setDateTime] = React.useState(data?.dateTime ?? "2026-07-03T09:15");
  const [location, setLocation] = React.useState(data?.location ?? "Room 204B - bathroom");
  const [description, setDescription] = React.useState(data?.description ??
    "Resident found on bathroom floor near the toilet; complaint of right hip pain. No loss of consciousness observed.");
  const [witnesses, setWitnesses] = React.useState(data?.witnesses ?? "Marcus Rivera, CNA (present at time of fall)");
  const [immediateAction, setImmediateAction] = React.useState(data?.immediateAction ?? "Assisted resident to bed, vitals taken, physician notified per facility protocol.");

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
            <Input
              className={inputClass}
              value={resident}
              onChange={(e) => setResident(e.target.value)}
            />
          </Field>

          <Field label="Incident Type" required>
            <select
              className={inputClass}
              value={incidentType}
              onChange={(e) => setIncidentType(e.target.value)}
            >
              <option>Fall</option>
              <option>Medication Error</option>
              <option>Skin Breakdown</option>
              <option>Other</option>
            </select>
          </Field>
        </div>

        <div className="grid gap-4 md:grid-cols-2">
          <Field label="Severity" required>
            <select
              className={inputClass}
              value={severity}
              onChange={(e) => setSeverity(e.target.value)}
            >
              <option>Major</option>
              <option>Moderate</option>
              <option>Minor</option>
            </select>
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
          <Button>Save</Button>
        </div>
      </div>
    </div>
  );
}