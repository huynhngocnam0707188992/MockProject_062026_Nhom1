import { useState } from "react";
import {
  Users,
  UserCheck,
  UserPlus,
  UserX,
} from "lucide-react";

import { StatisticCard } from "./ul-statistic-card";
import { SearchFilter } from "./ul-search-filter";
import { UserTable } from "./ul-table";

interface UserListProps {
  onAdd: () => void;
  onEdit: (user: any) => void;
}

export function UserList({
  onAdd,
  onEdit,
}: UserListProps) {
  const [search, setSearch] = useState("");
  const [role, setRole] = useState("ALL");
  const [status, setStatus] = useState("ALL");

  // Mock data chỉ để làm giao diện
  const users = [
    {
      id: "1",
      fullName: "Priya Shah",
      email: "priya.shah@nhms.io",
      phone: "--",
      role: "Admission",
      status: "Invited",
      twoFA: "Not-set",
      lastLogin: "--",
    },
    {
      id: "2",
      fullName: "Denise Carter",
      email: "denise.carter@nhms.io",
      phone: "+1 415-555-0142",
      role: "DON",
      status: "Active",
      twoFA: "Enabled",
      lastLogin: "2026-07-03 08:12",
    },
    {
      id: "3",
      fullName: "Anna Lee",
      email: "anna.lee@nhms.io",
      phone: "+1 415-555-0198",
      role: "Nurse",
      status: "Active",
      twoFA: "Enabled",
      lastLogin: "2026-07-03 07:40",
    },
    {
      id: "4",
      fullName: "Marcus Rivera",
      email: "marcus.rivera@nhms.io",
      phone: "+1 415-555-0173",
      role: "CNA",
      status: "Active",
      twoFA: "Enabled",
      lastLogin: "2026-07-02 22:05",
    },
    {
      id: "5",
      fullName: "Karen Wu",
      email: "karen.wu@nhms.io",
      phone: "+1 415-555-0166",
      role: "Billing",
      status: "Suspended",
      twoFA: "Enabled",
      lastLogin: "2026-06-20 14:22",
    },
    {
      id: "6",
      fullName: "Tom Becker",
      email: "tom.becker@nhms.io",
      phone: "+1 415-555-0184",
      role: "Nurse",
      status: "Deactivated",
      twoFA: "Enabled",
      lastLogin: "2026-05-15 10:00",
    },
    {
      id: "7",
      fullName: "Victor Alvarez",
      email: "victor.alvarez@nhms.io",
      phone: "+1 415-555-0110",
      role: "System Admin",
      status: "Active",
      twoFA: "Enabled",
      lastLogin: "2026-07-03 09:00",
    },
  ];

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <div>
        <p className="text-sm text-gray-400">
          Admin &gt; Users
        </p>

        <h1 className="mt-1 text-3xl font-bold text-gray-800">
          User List
        </h1>

        <p className="text-gray-500">
          All user accounts
        </p>
      </div>

      {/* Statistics */}
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
        <StatisticCard
          label="Total Users"
          value={7}
          icon={<Users size={20} />}
          iconBgClassName="bg-blue-100"
        />

        <StatisticCard
          label="Active"
          value={4}
          icon={<UserCheck size={20} />}
          iconBgClassName="bg-green-100"
        />

        <StatisticCard
          label="Invited"
          value={1}
          icon={<UserPlus size={20} />}
          iconBgClassName="bg-yellow-100"
        />

        <StatisticCard
          label="Suspended / Deactivated"
          value={2}
          icon={<UserX size={20} />}
          iconBgClassName="bg-red-100"
        />
      </div>

      {/* Search */}
      <SearchFilter
        search={search}
        role={role}
        status={status}
        onSearchChange={setSearch}
        onRoleChange={setRole}
        onStatusChange={setStatus}
        onReset={() => {}}
        onAdd={onAdd}
      />

      {/* Table */}
      <UserTable
        users={users}
        isLoading={false}
        onEdit={onEdit}
      />
    </div>
  );
}