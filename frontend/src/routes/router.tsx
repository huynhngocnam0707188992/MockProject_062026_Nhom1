import { createBrowserRouter, Navigate } from "react-router";
import { adminRoutes } from "./admin-routes";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Navigate to="/admin" replace />,
  },
  adminRoutes,
]);

