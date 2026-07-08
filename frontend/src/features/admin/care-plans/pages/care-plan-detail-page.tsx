import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { GoalTab } from "../tabs/goal-tab";
import { InterventionTab } from "../tabs/intervention-tab";
import { PERMISSIONS } from "@/common/permissions";
import { usePermissions } from "@/features/auth/hooks/use-current-user";

const tabs = [
  {
    value: "goals",
    label: "Goals",
    permission: PERMISSIONS.CARE_PLAN_VIEW,
    content: <GoalTab />,
  },
  {
    value: "interventions",
    label: "Interventions",
    permission: PERMISSIONS.CARE_PLAN_VIEW,
    content: <InterventionTab />,
  },
];

const CarePlanDetailPage = () => {
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

export default CarePlanDetailPage;
