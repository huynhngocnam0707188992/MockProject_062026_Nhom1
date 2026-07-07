import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ByDateTab } from "../tabs/by-date-tab";
import { ByResidentTab } from "../tabs/by-resident-tab";

const CareTaskPage = () => {
  return (
    <Tabs defaultValue="by-date">
      <TabsList className={"w-full"}>
        <TabsTrigger value="by-date">By Date</TabsTrigger>
        <TabsTrigger value="by-resident">By Resident</TabsTrigger>
      </TabsList>
      <TabsContent value="by-date">
        <ByDateTab />
      </TabsContent>
      <TabsContent value="by-resident">
        <ByResidentTab />
      </TabsContent>
    </Tabs>
  );
};

export default CareTaskPage;
