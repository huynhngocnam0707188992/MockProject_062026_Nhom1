import { Tabs, TabsContent, TabsList, TabsTrigger } from "../../ui/tabs";
import CarePlainDetailTabActivity from "../../pages/care-plan-detail/tab/care-plan-detail-tab-acitivity";
import CarePlanDetailTabOverview from "../../pages/care-plan-detail/tab/care-plan-detail-tab-overview";
import CarePlanDetailTabCost from "../../pages/care-plan-detail/tab/care-plan-detail-tab-cost";

export default function CarePlanNavBar() {
  return (
    <div>
      <Tabs defaultValue="carePlanDetail" className="w-full">
        <TabsList>
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="activity">Activity</TabsTrigger>
          <TabsTrigger value="cost">Cost</TabsTrigger>
        </TabsList>

        <TabsContent value="overview">
          <CarePlanDetailTabOverview></CarePlanDetailTabOverview>
        </TabsContent>

        <TabsContent value="activity">
          <CarePlainDetailTabActivity></CarePlainDetailTabActivity>
        </TabsContent>

        <TabsContent value="cost">
          <CarePlanDetailTabCost></CarePlanDetailTabCost>
        </TabsContent>
      </Tabs>
    </div>
  );
}
