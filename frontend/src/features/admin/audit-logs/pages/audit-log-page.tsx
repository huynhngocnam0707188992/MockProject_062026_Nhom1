import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { SystemActionTab } from "../tabs/system-action-tab";
import { PhiAccessTab } from "../tabs/phi-access-tab";

const AuditlogPage = () => {
  return (
    <Tabs defaultValue="system-actions">
      <TabsList className={"w-full"}>
        <TabsTrigger value="system-actions">System Actions</TabsTrigger>
        <TabsTrigger value="phi-access">PHI Access</TabsTrigger>
      </TabsList>
      <TabsContent value="system-actions">
        <SystemActionTab />
      </TabsContent>
      <TabsContent value="phi-access">
        <PhiAccessTab />
      </TabsContent>
    </Tabs>
  );
};

export default AuditlogPage;
