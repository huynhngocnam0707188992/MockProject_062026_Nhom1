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
    <div className="flex flex-col w-full gap-6 pb-6 max-w-full mx-auto px-6 py-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-border pb-4">
        <div className="flex flex-col">
          <h1 className="text-2xl font-semibold tracking-tight text-foreground">
            Care Tasks
          </h1>
          <p className="text-sm text-muted-foreground mt-1">
            Manage daily assignments and task execution.
          </p>
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue={visibleTabs[0]?.value}>
        <TabsList className="w-full sm:w-auto self-start">
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
    </div>
  );
};

export default CareTaskPage;
