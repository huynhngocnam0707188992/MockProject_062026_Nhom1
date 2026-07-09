import { Search } from "lucide-react";

interface SearchFilterProps {
  search: string;
  role: string;
  status: string;

  onSearchChange: (value: string) => void;
  onRoleChange: (value: string) => void;
  onStatusChange: (value: string) => void;

  onReset: () => void;

  // Thêm prop này
  onAdd: () => void;
}

export function SearchFilter({
  search,
  role,
  status,
  onSearchChange,
  onRoleChange,
  onStatusChange,
  onAdd,
}: SearchFilterProps) {
  return (
    <div className="flex flex-col gap-4 rounded-xl border border-gray-200 bg-white p-4 lg:flex-row lg:items-center lg:justify-between">
      <div className="flex flex-1 flex-col gap-3 md:flex-row">
        {/* Search */}
        <div className="relative flex-1">
          <Search
            size={18}
            className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
          />

          <input
            type="text"
            placeholder="Search by email or phone"
            value={search}
            onChange={(e) => onSearchChange(e.target.value)}
            className="w-full rounded-lg border border-gray-300 py-2.5 pl-10 pr-4 outline-none focus:border-blue-500"
          />
        </div>

        {/* Role */}
        <select
          value={role}
          onChange={(e) => onRoleChange(e.target.value)}
          className="rounded-lg border border-gray-300 px-4 py-2.5 outline-none focus:border-blue-500"
        >
          <option value="ALL">Role: All</option>
          <option value="System Admin">System Admin</option>
          <option value="Admission">Admission</option>
          <option value="DON">DON</option>
          <option value="Nurse">Nurse</option>
          <option value="CNA">CNA</option>
          <option value="Billing">Billing</option>
        </select>

        {/* Status */}
        <select
          value={status}
          onChange={(e) => onStatusChange(e.target.value)}
          className="rounded-lg border border-gray-300 px-4 py-2.5 outline-none focus:border-blue-500"
        >
          <option value="ALL">Status: All</option>
          <option value="Active">Active</option>
          <option value="Invited">Invited</option>
          <option value="Suspended">Suspended</option>
          <option value="Deactivated">Deactivated</option>
        </select>
      </div>

      {/* Add User */}
      <button
        type="button"
        onClick={onAdd}
        className="rounded-lg bg-blue-600 px-6 py-2.5 font-medium text-white transition hover:bg-blue-700"
      >
        + Add User
      </button>
    </div>
  );
}