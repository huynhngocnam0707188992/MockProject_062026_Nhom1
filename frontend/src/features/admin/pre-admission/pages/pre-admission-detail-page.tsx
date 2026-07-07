import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScreeningTab } from "../tabs/screening-tab";
import { AssessmentTab } from "../tabs/assessment-tab";

const PreAdmissionDetailPage = () => {
  return (
    <Tabs defaultValue="screening">
      <TabsList className={"w-full"}>
        <TabsTrigger value="screening">Screening</TabsTrigger>
        <TabsTrigger value="assessment">Assessment</TabsTrigger>
      </TabsList>
      <TabsContent value="screening">
        <ScreeningTab />
      </TabsContent>
      <TabsContent value="assessment">
        <AssessmentTab />
      </TabsContent>
    </Tabs>
  );
};

export default PreAdmissionDetailPage;
