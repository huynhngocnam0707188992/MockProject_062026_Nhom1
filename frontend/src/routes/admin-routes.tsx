import AdmissionPage from "@/features/admin/admissions/pages/admission-page";
import PreAdmissionPage from "@/features/admin/pre-admission/pages/pre-admission-page";
import PreAdmissionDetailPage from "@/features/admin/pre-admission/pages/pre-admission-detail-page";
import AuditlogPage from "@/features/admin/audit-logs/pages/audit-log-page";
import CarePlanPage from "@/features/admin/care-plans/pages/care-plan-page";
import CareTaskPage from "@/features/admin/care-tasks/pages/care-task-page";
import DashboardPage from "@/features/admin/dashboard/pages/dashboard-page";
import FacilityPage from "@/features/admin/facilities/pages/facility-page";
import NotificationPage from "@/features/admin/notifications/pages/notification-page";
import ProfilePage from "@/features/admin/profile/pages/profile-page";
import ResidentPage from "@/features/admin/residents/pages/resident-page";
import ResidentDetailPage from "@/features/admin/residents/pages/resident-detail-page";
import SettingPage from "@/features/admin/settings/pages/setting-page";
import { AdminLayout } from "@/layouts/admin-layout";
import { PERMISSIONS } from "@/common/permissions";
import type { RouteObject } from "react-router";
import { RequirePermission } from "@/components/common/require-permission";
import { FacilityDetailPage } from "@/features/admin/facilities/pages/facility-detail-page";
import CarePlanReviewPage from "@/features/admin/care-plans/pages/care-plan-review-page";
import CarePlanDetailPage from "@/features/admin/care-plans/pages/care-plan-detail/care-plan-detail-page";

export const adminRoutes: RouteObject = {
  path: "/admin",
  element: <AdminLayout />,
  children: [
    { index: true, element: <DashboardPage /> },
    {
      path: "residents",
      element: (
        <RequirePermission permission={PERMISSIONS.RESIDENT_VIEW}>
          <ResidentPage />
        </RequirePermission>
      ),
    },
    {
      path: "residents/:id",
      element: (
        <RequirePermission permission={PERMISSIONS.RESIDENT_VIEW}>
          <ResidentDetailPage />
        </RequirePermission>
      ),
    },
    {
      path: "pre-admission",
      element: (
        <RequirePermission
          permission={[PERMISSIONS.SCREENING_VIEW, PERMISSIONS.ASSESSMENT_VIEW]}
        >
          <PreAdmissionPage />
        </RequirePermission>
      ),
    },
    {
      path: "pre-admission/:id",
      element: (
        <RequirePermission
          permission={[PERMISSIONS.SCREENING_VIEW, PERMISSIONS.ASSESSMENT_VIEW]}
        >
          <PreAdmissionDetailPage />
        </RequirePermission>
      ),
    },
    {
      path: "admissions",
      element: (
        <RequirePermission permission={PERMISSIONS.ADMISSION_VIEW}>
          <AdmissionPage />
        </RequirePermission>
      ),
    },
    {
      path: "facilities",
      element: (
        <RequirePermission permission={PERMISSIONS.FACILITY_VIEW}>
          <FacilityPage />
        </RequirePermission>
      ),
    },
    {
      path: "facilities/:id",
      element: (
        <RequirePermission permission={PERMISSIONS.FACILITY_VIEW}>
          <FacilityDetailPage />
        </RequirePermission>
      ),
    },
    {
      path: "care-plans",
      element: (
        <RequirePermission permission={PERMISSIONS.CARE_PLAN_VIEW}>
          <CarePlanPage />
        </RequirePermission>
      ),
    },
    {
      path: "care-plans/:id",
      element: (
        <RequirePermission permission={PERMISSIONS.CARE_PLAN_VIEW}>
          <CarePlanDetailPage />

        </RequirePermission>
      ),
    },
    {
      path: "care-plans/review",
      element: (
        <RequirePermission permission={PERMISSIONS.CARE_PLAN_VIEW}>
          <CarePlanReviewPage />
        </RequirePermission>
      ),
    },
    {
      path: "care-tasks",
      element: (
        <RequirePermission permission={PERMISSIONS.CARE_TASK_VIEW}>
          <CareTaskPage />
        </RequirePermission>
      ),
    },
    {
      path: "audit-logs",
      element: (
        <RequirePermission
          permission={[
            PERMISSIONS.AUDIT_LOG_VIEW,
            PERMISSIONS.PHI_ACCESS_LOG_VIEW,
          ]}
        >
          <AuditlogPage />
        </RequirePermission>
      ),
    },
    {
      path: "notifications",
      element: (
        <RequirePermission permission={PERMISSIONS.NOTIFICATION_VIEW}>
          <NotificationPage />
        </RequirePermission>
      ),
    },
    {
      path: "settings",
      element: (
        <RequirePermission
          permission={[
            PERMISSIONS.ROLE_VIEW,
            PERMISSIONS.PERMISSION_VIEW,
            PERMISSIONS.USER_VIEW,
            PERMISSIONS.CARE_LEVEL_VIEW,
            PERMISSIONS.ASSESSMENT_METRIC_VIEW,
          ]}
        >
          <SettingPage />
        </RequirePermission>
      ),
    },
    { path: "profile", element: <ProfilePage /> },
  ],
};
