import {
  LayoutDashboard,
  Users,
  ClipboardCheck,
  ClipboardList,
  Building2,
  Stethoscope,
  ListChecks,
  ScrollText,
  Settings,
  Bell,
  UserCircle,
  type LucideIcon,
} from "lucide-react";

export interface AdminNavItem {
  label: string;
  path: string;
  icon: LucideIcon;
}

export interface AdminNavGroup {
  title: string;
  items: AdminNavItem[];
}

export const adminNavGroups: AdminNavGroup[] = [
  {
    title: "Overview",
    items: [{ label: "Dashboard", path: "/admin", icon: LayoutDashboard }],
  },
  {
    title: "Resident Intake",
    items: [
      { label: "Residents", path: "/admin/residents", icon: Users },
      {
        label: "Pre-Admission & Assessment",
        path: "/admin/pre-admission",
        icon: ClipboardCheck,
      },
      {
        label: "Admissions & Bed Assignment",
        path: "/admin/admissions",
        icon: ClipboardList,
      },
    ],
  },
  {
    title: "Facility",
    items: [
      {
        label: "Facilities & Rooms/Beds",
        path: "/admin/facilities",
        icon: Building2,
      },
    ],
  },
  {
    title: "Care Planning",
    items: [
      { label: "Care Plans", path: "/admin/care-plans", icon: Stethoscope },
      { label: "Care Tasks", path: "/admin/care-tasks", icon: ListChecks },
    ],
  },
  {
    title: "Compliance",
    items: [
      { label: "Audit Logs", path: "/admin/audit-logs", icon: ScrollText },
    ],
  },
  {
    title: "System",
    items: [
      { label: "Notifications", path: "/admin/notifications", icon: Bell },
      {
        label: "Settings & Permissions",
        path: "/admin/settings",
        icon: Settings,
      },
      { label: "Profile", path: "/admin/profile", icon: UserCircle },
    ],
  },
];
