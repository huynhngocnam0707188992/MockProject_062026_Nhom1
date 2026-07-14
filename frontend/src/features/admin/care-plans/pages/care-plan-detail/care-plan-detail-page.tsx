// import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
// import { GoalTab } from "../tabs/goal-tab";
// import { InterventionTab } from "../tabs/intervention-tab";

import CarePlanNavBar from "../../components/care-plan-detail/care-plan-detail-tabs";
import CarePlanDetailTitle from "../../components/care-plan-detail/care-plan-detail-title";
import CarePlanTitle from "../../components/care-plan-title";
import Flag from "../../ui/flag";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";

// /*Deprecated*/

// const CarePlanDetailPage = () => {
//   return (
//     <Tabs defaultValue="goals">
//       <TabsList className={"w-full"}>
//         <TabsTrigger value="goals">Goals</TabsTrigger>
//         <TabsTrigger value="interventions">Interventions</TabsTrigger>
//       </TabsList>
//       <TabsContent value="goals">
//         <GoalTab />
//       </TabsContent>
//       <TabsContent value="interventions">
//         <InterventionTab />
//       </TabsContent>
//     </Tabs>
//   );
// };

// export default CarePlanDetailPage;

type CarePlanDetailProps = {
  id?: string;
  residentName?: string;
  status?: string;
  room?: string;
  locTier?: string;
  nextReview?: string;
};
export default function CarePlanDetailPage({
  id,
  residentName,
  status,
  room,
  locTier,
  nextReview,
}: CarePlanDetailProps) {
  return (
    <div>
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans">
              Care Planning
            </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans/2">Detail</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans/2">Name</BreadcrumbLink>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
      <div className="flex items-center">
        <div className="mr-[16px]">
          <CarePlanDetailTitle
            residentName={residentName}
          ></CarePlanDetailTitle>
        </div>
        <Flag
          title={status ?? "Active"}
          className={`rounded-full ${
            status === "Needs Update"
              ? "bg-red-300 text-red-700 border-red-400"
              : "bg-green-300 text-green-700 border-green-400"
          }`}
        ></Flag>
      </div>
      <div className=" text-gray-600">
        {`Room ${room ?? "204B"} . LOC Tier ${locTier ?? "3"} . Next review ${nextReview ?? "2026-07-07"}`}
      </div>

      <div className="tab mt-4">
        <CarePlanNavBar></CarePlanNavBar>
      </div>
    </div>
  );
}
