import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ByDateTab } from "../tabs/by-date-tab";
import { ByResidentTab } from "../tabs/by-resident-tab";
import { PERMISSIONS } from "@/common/permissions";
import { usePermissions } from "@/features/auth/hooks/use-current-user";

const tabs = [
  {
    value: "by-date",
    label: "By Date",
    permission: PERMISSIONS.CARE_TASK_VIEW,
    content: <ByDateTab />,
  },
  {
    value: "by-resident",
    label: "By Resident",
    permission: PERMISSIONS.CARE_TASK_VIEW,
    content: <ByResidentTab />,
  },
];

const CareTaskPage = () => {
  const { can } = usePermissions();
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

export default CareTaskPage;
