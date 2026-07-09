import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScreeningTab } from "../tabs/screening-tab";
import { AssessmentTab } from "../tabs/assessment-tab";
import { PERMISSIONS } from "@/common/permissions";
import { usePermissions } from "@/features/auth/hooks/use-current-user";

const tabs = [
  {
    value: "screening",
    label: "Screening",
    permission: PERMISSIONS.SCREENING_VIEW,
    content: <ScreeningTab />,
  },
  {
    value: "assessment",
    label: "Assessment",
    permission: PERMISSIONS.ASSESSMENT_VIEW,
    content: <AssessmentTab />,
  },
];

const PreAdmissionDetailPage = () => {
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

export default PreAdmissionDetailPage;
