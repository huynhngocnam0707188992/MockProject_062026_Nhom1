import { Info, Save } from "lucide-react";
import { AdminPageShell, AdminPageShellNote } from "@/components/common/admin-page-shell";
import { AdminPageActions } from "@/components/common/admin-page-actions";
import { SeverityRow } from "@/components/common/severity-row";

const SlaConfigPage = () => {
  const handleCancel = () => {
    console.log("SLA Config cancel clicked");
  };

  const handleSave = () => {
    console.log("SLA Config save clicked");
  };

  const footer = (
    <AdminPageActions
      onCancel={handleCancel}
      onSave={handleSave}
      saveIcon={<Save className="w-4 h-4" />}
    />
  );

  return (
    <AdminPageShell
      breadcrumbs={[
        { label: "Admin" },
        { label: "SLA Config", active: true },
      ]}
      title="SLA Configuration"
      subtitle="Regulatory reporting deadlines by incident severity (NFR-06)"
      infoBanner={{
        icon: <Info className="w-5 h-5" />,
        text: "Deadlines fixed to AD-08 severity tiers. Simulated — nothing is transmitted externally (NFR-05).",
      }}
      footer={footer}
    >
      <div className="glass-card rounded-2xl overflow-hidden shadow-sm">
        <div className="overflow-x-auto w-full">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-surface-container-low border-b border-outline-variant/30">
                <th className="px-6 py-4 font-label-bold text-sm text-outline uppercase tracking-wider">
                  Severity
                </th>
                <th className="px-6 py-4 font-label-bold text-sm text-outline uppercase tracking-wider">
                  External Report Required
                </th>
                <th className="px-6 py-4 font-label-bold text-sm text-outline uppercase tracking-wider">
                  Reporting Deadline
                </th>
                <th className="px-6 py-4 font-label-bold text-sm text-outline uppercase tracking-wider">
                  Regulatory Body
                </th>
                <th className="px-6 py-4 font-label-bold text-sm text-outline uppercase tracking-wider text-right"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-outline-variant/20">
              <SeverityRow level="Critical" badgeClasses="bg-[rgb(254,226,226)] text-[rgb(220,38,38)] border border-error/20">
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface">Yes</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface font-bold">24 hours</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface-variant">CA Dept. of Public Health</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Major" badgeClasses="bg-[rgb(255,237,213)] text-[rgb(194,65,12)] border border-tertiary-fixed-dim/50">
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface">Yes</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface font-bold">24 hours</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface-variant">CA Dept. of Public Health</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Moderate" badgeClasses="bg-[rgb(254,243,199)] text-[rgb(217,119,6)] border border-secondary-fixed-dim/50">
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface">Yes</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface font-bold">48 hours</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface-variant">CA Dept. of Public Health</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Minor" badgeClasses="bg-[rgb(243,244,246)] text-[rgb(109,116,129)] border border-outline-variant/50">
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-on-surface-variant">No</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-outline">— (not required)</p>
                </td>
                <td className="px-6 py-6">
                  <p className="text-body-base font-body-base text-outline">—</p>
                </td>
              </SeverityRow>
            </tbody>
          </table>
        </div>
        <AdminPageShellNote>
          Only Deadline and Regulatory Body are editable. Values match M7 Incident List (SLA Countdown), Incident Detail, and Submit External Report modal (S6) — e.g. Major=24h, Moderate=48h window.
        </AdminPageShellNote>
      </div>
    </AdminPageShell>
  );
};

export default SlaConfigPage;
