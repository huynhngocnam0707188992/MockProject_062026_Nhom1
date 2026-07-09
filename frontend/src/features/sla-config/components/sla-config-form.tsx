import React, { useState, useCallback } from 'react';
import { Calendar, Save, CheckCircle2, RefreshCw } from 'lucide-react';
import { Button } from '@/components/ui/button';

export interface SlaRowState {
  severity: string;
  colorClass: 'critical' | 'high' | 'medium' | 'low' | 'info';
  hours: number;
  percent: number;
  target: string;
}

const INITIAL_ROWS: SlaRowState[] = [
  { severity: 'Critical', colorClass: 'critical', hours: 2, percent: 10, target: 'Emergency Response Team' },
  { severity: 'High', colorClass: 'high', hours: 8, percent: 25, target: 'Director of Nursing (DON)' },
  { severity: 'Medium', colorClass: 'medium', hours: 24, percent: 50, target: 'Nursing Supervisor' },
  { severity: 'Low', colorClass: 'low', hours: 48, percent: 75, target: 'Quality Assurance Committee' },
  { severity: 'Information Only', colorClass: 'info', hours: 72, percent: 90, target: 'Facility Administrator' }
];

export function SlaConfigForm(): React.ReactElement {
  const [rows, setRows] = useState<SlaRowState[]>(INITIAL_ROWS);
  const [effectiveDate, setEffectiveDate] = useState<string>('2026-06-29');

  // Boolean states prefixed with standard conventions
  const [isSaving, setIsSaving] = useState<boolean>(false);
  const [hasSaved, setHasSaved] = useState<boolean>(false);

  // Sync range/text updates for SLA Window Hours
  const handleHoursChange = useCallback((index: number, value: string) => {
    const parsedHours = parseInt(value, 10) || 0;
    setRows((prevRows) => {
      const updatedRows = [...prevRows];
      updatedRows[index] = { ...updatedRows[index], hours: parsedHours };
      return updatedRows;
    });
    if (hasSaved) setHasSaved(false);
  }, [hasSaved]);

  // Sync slider and text input value updates for Escalation %
  const handlePercentChange = useCallback((index: number, value: string) => {
    let parsedPercent = parseInt(value, 10) || 0;
    if (parsedPercent < 0) parsedPercent = 0;
    if (parsedPercent > 100) parsedPercent = 100;

    setRows((prevRows) => {
      const updatedRows = [...prevRows];
      updatedRows[index] = { ...updatedRows[index], percent: parsedPercent };
      return updatedRows;
    });
    if (hasSaved) setHasSaved(false);
  }, [hasSaved]);

  // Handle dropdown target select updates
  const handleTargetChange = useCallback((index: number, value: string) => {
    setRows((prevRows) => {
      const updatedRows = [...prevRows];
      updatedRows[index] = { ...updatedRows[index], target: value };
      return updatedRows;
    });
    if (hasSaved) setHasSaved(false);
  }, [hasSaved]);

  // Handle Datepicker Updates
  const handleDateChange = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    setEffectiveDate(event.target.value);
    if (hasSaved) setHasSaved(false);
  }, [hasSaved]);

  // Handle Save
  const handleFormSave = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSaving(true);
    setHasSaved(false);

    try {
      // Simulate API saving call
      await new Promise((resolve) => setTimeout(resolve, 1500));
      setHasSaved(true);
    } catch (error) {
      console.error('Failed to save SLA configurations', error);
    } finally {
      setIsSaving(false);
    }
  };

  // Handle Cancel Reset
  const handleCancelReset = useCallback(() => {
    setRows(INITIAL_ROWS);
    setEffectiveDate('2026-06-29');
    setHasSaved(false);
  }, []);

  return (
    <form onSubmit={handleFormSave} noValidate>
      {hasSaved && (
        <div className="sla-success-message" role="alert">
          <CheckCircle2 size={18} />
          <span>SLA rules and escalation configurations saved successfully.</span>
        </div>
      )}

      {/* Table Section */}
      <div className="sla-table-wrapper">
        <table className="sla-config-table" aria-label="Incident Severity SLA Configuration table">
          <thead className="sla-table-hdr-row">
            <tr>
              <th scope="col" className="sla-table-hdr-cell">Severity</th>
              <th scope="col" className="sla-table-hdr-cell">SLA Window (Hours)</th>
              <th scope="col" className="sla-table-hdr-cell">Escalation At (% Elapsed)</th>
              <th scope="col" className="sla-table-hdr-cell">Notification Target</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((row, index) => (
              <tr key={row.severity} className="sla-table-row">
                {/* Column 1: Severity Label */}
                <td className="sla-table-cell">
                  <div className="severity-col-box">
                    <span className={`severity-color-strip ${row.colorClass}`} aria-hidden="true" />
                    <span>{row.severity}</span>
                  </div>
                </td>

                {/* Column 2: Hours Input */}
                <td className="sla-table-cell">
                  <div className="hours-input-wrapper">
                    <input
                      type="number"
                      className="sla-hours-field"
                      value={row.hours === 0 ? '' : row.hours}
                      onChange={(e) => handleHoursChange(index, e.target.value)}
                      disabled={isSaving}
                      min={1}
                      max={9999}
                      required
                    />
                    <span>hrs</span>
                  </div>
                </td>

                {/* Column 3: Slider Progress + Percent Input */}
                <td className="sla-table-cell">
                  <div className="escalation-slider-col">
                    <div className="slider-label-row">
                      <span className="slider-status-text">{row.percent}% elapsed</span>
                    </div>
                    <div className="slider-input-track-wrapper">
                      <input
                        type="range"
                        className="escalation-input-range"
                        value={row.percent}
                        onChange={(e) => handlePercentChange(index, e.target.value)}
                        disabled={isSaving}
                        min={0}
                        max={100}
                      />
                      <div className="sla-percent-field-box">
                        <input
                          type="number"
                          className="sla-percent-field"
                          value={row.percent}
                          onChange={(e) => handlePercentChange(index, e.target.value)}
                          disabled={isSaving}
                          min={0}
                          max={100}
                        />
                        <span>%</span>
                      </div>
                    </div>
                  </div>
                </td>

                {/* Column 4: Notification Target Dropdown */}
                <td className="sla-table-cell">
                  <div className="target-dropdown-col">
                    <select
                      value={row.target}
                      onChange={(e) => handleTargetChange(index, e.target.value)}
                      disabled={isSaving}
                      className="w-full bg-transparent border border-gray-300 rounded px-2 py-1"
                    >
                      <option value="Emergency Response Team">Emergency Response Team</option>
                      <option value="Director of Nursing (DON)">Director of Nursing (DON)</option>
                      <option value="Nursing Supervisor">Nursing Supervisor</option>
                      <option value="Quality Assurance Committee">Quality Assurance Committee</option>
                      <option value="Facility Administrator">Facility Administrator</option>
                    </select>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Date Picker Section */}
      <div className="date-picker-row">
        <label htmlFor="sla-effective-date" className="datepicker-label">
          Effective From Date
        </label>
        <div className="datepicker-input-wrapper">
          <Calendar size={18} />
          <input
            id="sla-effective-date"
            type="date"
            className="datepicker-field"
            value={effectiveDate}
            onChange={handleDateChange}
            disabled={isSaving}
          />
        </div>
        <span className="datepicker-caption">
          Changes will apply to all new incidents created after this date.
        </span>
      </div>

      {/* Form Action Controls */}
      <div className="sla-actions-row">
        <Button
          id="sla-save-button"
          type="submit"
          className="sla-save-btn"
          disabled={isSaving}
        >
          {isSaving ? (
            <>
              <RefreshCw className="animate-spin" size={16} />
              <span>Saving...</span>
            </>
          ) : (
            <>
              <Save size={16} />
              <span>Save SLA Rules</span>
            </>
          )}
        </Button>
        <button
          type="button"
          className="sla-cancel-link"
          onClick={handleCancelReset}
          disabled={isSaving}
        >
          Cancel Changes
        </button>
      </div>
    </form>
  );
}
