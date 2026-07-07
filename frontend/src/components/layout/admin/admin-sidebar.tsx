import { NavLink } from "react-router";
import { HeartPulse } from "lucide-react";
import { cn } from "@/lib/utils";
import { adminNavGroups } from "@/common/admin-nav";

export const AdminSidebar = () => {
  return (
    <aside className="w-64 shrink-0 border-r border-sidebar-border bg-sidebar flex flex-col h-screen sticky top-0">
      <div className="h-16 flex items-center gap-2 px-5 border-b border-sidebar-border">
        <HeartPulse className="w-6 h-6 text-primary" />
        <span className="font-semibold text-sidebar-foreground">
          ElderCare Admin
        </span>
      </div>

      <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-5">
        {adminNavGroups.map((group) => (
          <div key={group.title}>
            <p className="px-3 mb-1 text-xs font-medium uppercase text-muted-foreground">
              {group.title}
            </p>
            <div className="space-y-0.5">
              {group.items.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  end={item.path === "/admin"}
                  className={({ isActive }) =>
                    cn(
                      "flex items-center gap-3 rounded-md px-3 py-2 text-sm text-sidebar-foreground hover:bg-sidebar-accent",
                      isActive &&
                        "bg-sidebar-accent text-sidebar-accent-foreground font-medium",
                    )
                  }
                >
                  <item.icon className="w-4 h-4" />
                  {item.label}
                </NavLink>
              ))}
            </div>
          </div>
        ))}
      </nav>
    </aside>
  );
};
