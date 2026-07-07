import AdmissionPage from "@/features/admin/admissions/pages/admission-page";
import PreAdmissionPage from "@/features/admin/pre-admission/pages/pre-admission-page";
import AuditlogPage from "@/features/admin/audit-logs/pages/audit-log-page";
import CarePlanPage from "@/features/admin/care-plans/pages/care-plan-page";
import CareTaskPage from "@/features/admin/care-tasks/pages/care-task-page";
import DashboardPage from "@/features/admin/dashboard/pages/dashboard-page";
import FacilityPage from "@/features/admin/facilities/pages/facility-page";
import NotificationPage from "@/features/admin/notifications/pages/notification-page";
import ProfilePage from "@/features/admin/profile/pages/profile-page";
import ResidentPage from "@/features/admin/residents/pages/resident-page";
import SettingPage from "@/features/admin/settings/pages/setting-page";
import { AdminLayout } from "@/layouts/admin-layout";
import type { RouteObject } from "react-router-dom";

export const adminRoutes: RouteObject = {
  path: "/admin",
  element: <AdminLayout />,
  children: [
    { index: true, element: <DashboardPage /> },
    { path: "residents", element: <ResidentPage /> },
    { path: "pre-admission", element: <PreAdmissionPage /> },
    { path: "admissions", element: <AdmissionPage /> },
    { path: "facilities", element: <FacilityPage /> },
    { path: "care-plans", element: <CarePlanPage /> },
    { path: "care-tasks", element: <CareTaskPage /> },
    { path: "audit-logs", element: <AuditlogPage /> },
    { path: "notifications", element: <NotificationPage /> },
    { path: "settings", element: <SettingPage /> },
    { path: "profile", element: <ProfilePage /> },
  ],
};
