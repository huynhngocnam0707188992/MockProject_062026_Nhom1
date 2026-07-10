import { useState, useEffect } from "react";
import { adminService } from "../../services/admin-service";
import { Button } from "@/components/ui/button";

interface MockUser {
  id: number;
  name: string;
  email: string;
  status: "ACTIVE" | "INACTIVE" | "LOCKED";
}

export const UserTab = () => {
  const [users, setUsers] = useState<MockUser[]>([]);
  const [loadingId, setLoadingId] = useState<number | null>(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await adminService.GetUsers();
        setUsers(response);
      } catch (err) {
        console.error("Failed to fetch users", err);
      }
    };
    fetchUsers();
  }, []);

  const toggleStatus = async (user: MockUser) => {
    const newStatus = user.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    setLoadingId(user.id);
    try {
      await adminService.ChangeUserStatus(user.id, newStatus);
      setUsers(users.map(u => u.id === user.id ? { ...u, status: newStatus } : u));
    } catch (err) {
      console.error("Failed to change status", err);
      alert("Failed to change user status.");
    } finally {
      setLoadingId(null);
    }
  };

  return (
    <div className="p-4 bg-white rounded shadow">
      <h3 className="text-lg font-semibold mb-4">Users Management</h3>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {users.map((user) => (
              <tr key={user.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{user.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{user.email}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm">
                  <span className={`w-20 justify-center inline-flex px-2 text-xs leading-5 font-semibold rounded-full ${
                    user.status === "ACTIVE" ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"
                  }`}>
                    {user.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <Button 
                    variant="outline" 
                    size="sm"
                    className="w-28"
                    onClick={() => toggleStatus(user)}
                    disabled={loadingId === user.id}
                  >
                    {loadingId === user.id ? "Updating..." : (user.status === "ACTIVE" ? "Deactivate" : "Activate")}
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};