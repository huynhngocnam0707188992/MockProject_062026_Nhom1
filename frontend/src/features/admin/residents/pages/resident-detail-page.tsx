import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { InfoTab } from "../tabs/info-tab";
import { ContactTab } from "../tabs/contact-tab";
import { CareLevelHistoryTab } from "../tabs/care-level-history";
import { SensitiveInfoTab } from "../tabs/sensitive-info";

const ResidentDetailPage = () => {
  return (
    <Tabs defaultValue="info">
      <TabsList className={"w-full"}>
        <TabsTrigger value="info">Info</TabsTrigger>
        <TabsTrigger value="contacts">Contacts</TabsTrigger>
        <TabsTrigger value="care-level-history">Care Level History</TabsTrigger>
        <TabsTrigger value="sensitive-info">Sensitive Info</TabsTrigger>
      </TabsList>
      <TabsContent value="info">
        <InfoTab />
      </TabsContent>
      <TabsContent value="contacts">
        <ContactTab />
      </TabsContent>
      <TabsContent value="care-level-history">
        <CareLevelHistoryTab />
      </TabsContent>
      <TabsContent value="sensitive-info">
        <SensitiveInfoTab />
      </TabsContent>
    </Tabs>
  );
};

export default ResidentDetailPage;
