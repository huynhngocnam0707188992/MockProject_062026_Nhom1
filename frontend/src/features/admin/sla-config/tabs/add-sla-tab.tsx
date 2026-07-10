import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import type { IncidentSeverityResponse } from "@/features/admin/incident-severity/services/incident-severity-service";

interface AddSLATabProps {
  severityOptions: IncidentSeverityResponse[] | undefined;
  selectedSeverityId: number | null;
  slaWindowHrs: string;
  errorMessage: string | null;
  onSeverityChange: (value: number) => void;
  onSLAWindowChange: (value: string) => void;
  onCreate: () => void;
}

export const AddSLATab = ({
  severityOptions,
  selectedSeverityId,
  slaWindowHrs,
  errorMessage,
  onSeverityChange,
  onSLAWindowChange,
  onCreate,
}: AddSLATabProps) => {
  return (
    <div className="space-y-6 p-6">
      <div>
        <label className="block text-sm font-medium text-on-surface mb-2">Severity tier</label>
        <select
          value={selectedSeverityId ?? ""}
          onChange={(event) => onSeverityChange(Number(event.target.value))}
          className="w-full rounded-lg border border-outline px-3 py-2 text-sm text-on-surface"
        >
          {severityOptions?.map((severity) => (
            <option key={severity.id} value={severity.id}>
              {severity.levelName}
            </option>
          ))}
        </select>
      </div>
      <div>
        <label className="block text-sm font-medium text-on-surface mb-2">SLA window (hours)</label>
        <Input
          type="number"
          value={slaWindowHrs}
          onChange={(event) => onSLAWindowChange(event.target.value)}
          placeholder="e.g. 24"
        />
      </div>
      {errorMessage ? <p className="text-sm text-destructive">{errorMessage}</p> : null}
      <div className="flex justify-end gap-3">
        <Button variant="default" onClick={onCreate}>
          Create SLA
        </Button>
      </div>
    </div>
  );
};
