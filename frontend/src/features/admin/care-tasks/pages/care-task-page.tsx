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
    <div className="flex flex-col w-full gap-stack-lg pb-stack-lg">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-stack-md border-b border-outline-variant pb-px mb-2">
        <div className="flex flex-col mb-4">
          <h1 className="font-headline-xl text-headline-xl text-on-surface">Care Tasks</h1>
          <p className="font-body-md text-body-md text-on-surface-variant mt-1">
            Manage daily assignments and task execution.
          </p>
        </div>
      </div>

      <Tabs defaultValue={visibleTabs[0]?.value}>
        <TabsList className="w-full sm:w-auto self-start bg-surface-container p-1 rounded-full">
          {visibleTabs.map((t) => (
            <TabsTrigger 
              key={t.value} 
              value={t.value}
              className="rounded-full px-6 py-2 data-[state=active]:bg-surface-container-lowest data-[state=active]:text-on-surface data-[state=active]:shadow-sm"
            >
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
    </div>
  );
};

export default CareTaskPage;
