import { ChevronRight, Info, Save } from "lucide-react";
import { AdminPageShell, AdminPageShellNote } from "@/components/common/admin-page-shell";
import { AdminPageActions } from "@/components/common/admin-page-actions";
import { SeverityRow } from "@/components/common/severity-row";

const IncidentSeverityPage = () => {
  const handleCancel = () => {
    console.log("Incident Severity cancel clicked");
  };

  const handleSave = () => {
    console.log("Incident Severity save clicked");
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
        { label: "Incident Severity", active: true },
      ]}
      title="Incident Severity Levels"
      subtitle="Taxonomy used across Incident & Risk (M7) — 4 levels, fixed"
      infoBanner={{
        icon: <Info className="w-5 h-5" />,
        text: "Fixed 4-level taxonomy, referenced across all M7 Incident wireframes. Only descriptions are editable.",
      }}
      footer={footer}
    >
      <div className="glass-card rounded-2xl overflow-hidden shadow-sm">
        <div className="overflow-x-auto w-full">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-surface-container-low border-b border-outline-variant/30">
                <th className="px-6 py-4 font-label-bold text-label-bold text-outline uppercase tracking-wider w-32">
                  Level
                </th>
                <th className="px-6 py-4 font-label-bold text-label-bold text-outline uppercase tracking-wider">
                  Description
                </th>
                <th className="px-6 py-4 font-label-bold text-label-bold text-outline uppercase tracking-wider">
                  Example
                </th>
                <th className="px-6 py-4 font-label-bold text-label-bold text-outline uppercase tracking-wider text-right">
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-outline-variant/20">
              <SeverityRow level="Minor" badgeClasses="bg-surface-container-low text-on-surface-variant border-outline-variant/50">
                <td className="px-6 py-6 align-top">
                  <p className="text-body-base font-body-base text-on-surface">Low-risk event; no injury or intervention required.</p>
                </td>
                <td className="px-6 py-6 align-top">
                  <p className="text-body-sm font-body-sm text-on-surface-variant italic">e.g. minor skin tear, no treatment needed</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Moderate" badgeClasses="bg-[rgb(254,243,199)] text-[rgb(217,119,6)] border-[rgb(252,216,133)]">
                <td className="px-6 py-6 align-top">
                  <p className="text-body-base font-body-base text-on-surface">Injury requiring minor treatment; no hospitalization.</p>
                </td>
                <td className="px-6 py-6 align-top">
                  <p className="text-body-sm font-body-sm text-on-surface-variant italic">e.g. bruise requiring first aid, missed dose</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Major" badgeClasses="bg-[rgb(255,237,213)] text-[rgb(194,65,12)] border-[rgb(255,210,156)]">
                <td className="px-6 py-6 align-top">
                  <p className="text-body-base font-body-base text-on-surface">Significant injury requiring treatment; possible hospitalization.</p>
                </td>
                <td className="px-6 py-6 align-top">
                  <p className="text-body-sm font-body-sm text-on-surface-variant italic">e.g. fall with fracture, med error</p>
                </td>
              </SeverityRow>
              <SeverityRow level="Critical" badgeClasses="bg-[rgb(254,226,226)] text-[rgb(220,38,38)] border-[rgb(254,202,202)]">
                <td className="px-6 py-6 align-top">
                  <p className="text-body-base font-body-base text-on-surface">Life-threatening event requiring emergency intervention.</p>
                </td>
                <td className="px-6 py-6 align-top">
                  <p className="text-body-sm font-body-sm text-on-surface-variant italic">e.g. elopement, cardiac event</p>
                </td>
              </SeverityRow>
            </tbody>
          </table>
        </div>
        <AdminPageShellNote>
          Levels cannot be added or deleted here. Chart-lock (BR-07) triggers on every incident regardless of severity — severity only determines the external-reporting deadline (see SLA Configuration, AD-09).
        </AdminPageShellNote>
      </div>
    </AdminPageShell>
  );
};

export default IncidentSeverityPage;
