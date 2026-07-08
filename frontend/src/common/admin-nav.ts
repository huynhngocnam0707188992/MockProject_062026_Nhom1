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
import { PERMISSIONS, type PermissionCode } from "@/common/permissions";

export interface AdminNavItem {
  label: string;
  path: string;
  icon: LucideIcon;
  permission: PermissionCode | PermissionCode[];
}

export interface AdminNavGroup {
  title: string;
  items: AdminNavItem[];
}

export const adminNavGroups: AdminNavGroup[] = [
  {
    title: "Overview",
    items: [
      {
        label: "Dashboard",
        path: "/admin",
        icon: LayoutDashboard,
        permission: PERMISSIONS.RESIDENT_VIEW,
      },
    ],
  },
  {
    title: "Resident Intake",
    items: [
      {
        label: "Residents",
        path: "/admin/residents",
        icon: Users,
        permission: PERMISSIONS.RESIDENT_VIEW,
      },
      {
        label: "Pre-Admission & Assessment",
        path: "/admin/pre-admission",
        icon: ClipboardCheck,
        permission: [PERMISSIONS.SCREENING_VIEW, PERMISSIONS.ASSESSMENT_VIEW],
      },
      {
        label: "Admissions & Bed Assignment",
        path: "/admin/admissions",
        icon: ClipboardList,
        permission: PERMISSIONS.ADMISSION_VIEW,
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
        permission: PERMISSIONS.FACILITY_VIEW,
      },
    ],
  },
  {
    title: "Care Planning",
    items: [
      {
        label: "Care Plans",
        path: "/admin/care-plans",
        icon: Stethoscope,
        permission: PERMISSIONS.CARE_PLAN_VIEW,
      },
      {
        label: "Care Tasks",
        path: "/admin/care-tasks",
        icon: ListChecks,
        permission: PERMISSIONS.CARE_TASK_VIEW,
      },
    ],
  },
  {
    title: "Compliance",
    items: [
      {
        label: "Audit Logs",
        path: "/admin/audit-logs",
        icon: ScrollText,
        permission: [
          PERMISSIONS.AUDIT_LOG_VIEW,
          PERMISSIONS.PHI_ACCESS_LOG_VIEW,
        ],
      },
    ],
  },
  {
    title: "System",
    items: [
      {
        label: "Notifications",
        path: "/admin/notifications",
        icon: Bell,
        permission: PERMISSIONS.NOTIFICATION_VIEW,
      },
      {
        label: "Settings & Permissions",
        path: "/admin/settings",
        icon: Settings,
        permission: [
          PERMISSIONS.ROLE_VIEW,
          PERMISSIONS.PERMISSION_VIEW,
          PERMISSIONS.USER_VIEW,
          PERMISSIONS.CARE_LEVEL_VIEW,
          PERMISSIONS.ASSESSMENT_METRIC_VIEW,
        ],
      },
      {
        label: "Profile",
        path: "/admin/profile",
        icon: UserCircle,
        permission: PERMISSIONS.RESIDENT_VIEW,
      },
    ],
  },
];
