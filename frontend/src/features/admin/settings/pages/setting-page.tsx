import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { RoleTab } from "../tabs/role-tab";
import { PermissionTab } from "../tabs/permission-tab";
import { UserTab } from "../tabs/user-tab";

const SettingPage = () => {
  return (
    <Tabs defaultValue="roles">
      <TabsList className={"w-full"}>
        <TabsTrigger value="roles">Roles</TabsTrigger>
        <TabsTrigger value="permissions">Permissions</TabsTrigger>
        <TabsTrigger value="users">Users</TabsTrigger>
      </TabsList>
      <TabsContent value="roles">
        <RoleTab />
      </TabsContent>
      <TabsContent value="permissions">
        <PermissionTab />
      </TabsContent>
      <TabsContent value="users">
        <UserTab />
      </TabsContent>
    </Tabs>
  );
};

export default SettingPage;
