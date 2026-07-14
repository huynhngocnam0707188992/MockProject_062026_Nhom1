import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { InfoTab } from "../tabs/info-tab";
import { ContactTab } from "../tabs/contact-tab";
import { CareLevelHistoryTab } from "../tabs/care-level-history";
import { SensitiveInfoTab } from "../tabs/sensitive-info";
import { PERMISSIONS } from "@/common/permissions";
import { usePermissions } from "@/features/auth/hooks/use-current-user";
import { useParams } from "react-router";
import LocResultTab from "../tabs/loc-result-tab";

const ResidentDetailPage = () => {
  const { can } = usePermissions();
  const { id } = useParams();
  const residentId = Number(id);

  const tabs = [
    {
      value: "info",
      label: "Info",
      permission: PERMISSIONS.RESIDENT_VIEW,
      content: <InfoTab />,
    },
    {
      value: "contacts",
      label: "Contacts",
      permission: PERMISSIONS.RESIDENT_VIEW,
      content: <ContactTab />,
    },
    {
      value: "care-level-history",
      label: "Care Level History",
      permission: PERMISSIONS.RESIDENT_VIEW,
      content: <CareLevelHistoryTab />,
    },
    {
      value: "loc-result",
      label: "LOC Classification Result",
      permission: PERMISSIONS.RESIDENT_VIEW,
      content: <LocResultTab residentId={residentId} />,
    },
    {
      value: "sensitive-info",
      label: "Sensitive Info",
      permission: PERMISSIONS.RESIDENT_SENSITIVE_VIEW,
      content: <SensitiveInfoTab />,
    },
  ];

  const visibleTabs = tabs.filter((t) => can(t.permission));

  return (
    <Tabs defaultValue={visibleTabs[0]?.value}>
      <TabsList className="w-full">
        {visibleTabs.map((t) => (
          <TabsTrigger key={t.value} value={t.value}>
            {t.label}
          </TabsTrigger>
        ))}
      </TabsList>
      {visibleTabs.map((t) => (
        <TabsContent key={t.value} value={t.value}>
          {t.content}
        </TabsContent>
      ))}
    </Tabs>
  );
};

export default ResidentDetailPage;