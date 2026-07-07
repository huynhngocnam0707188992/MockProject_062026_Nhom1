import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { GoalTab } from "../tabs/goal-tab";
import { InterventionTab } from "../tabs/intervention-tab";

const CarePlanDetailPage = () => {
  return (
    <Tabs defaultValue="goals">
      <TabsList className={"w-full"}>
        <TabsTrigger value="goals">Goals</TabsTrigger>
        <TabsTrigger value="interventions">Interventions</TabsTrigger>
      </TabsList>
      <TabsContent value="goals">
        <GoalTab />
      </TabsContent>
      <TabsContent value="interventions">
        <InterventionTab />
      </TabsContent>
    </Tabs>
  );
};

export default CarePlanDetailPage;
