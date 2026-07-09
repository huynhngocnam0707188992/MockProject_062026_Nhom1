import { ChevronLeft, ChevronRight } from "lucide-react";

export const Pagination = () => {
  return (
    <div className="px-6 py-4 border-t border-outline-variant bg-surface flex flex-col sm:flex-row items-center justify-between gap-4">
      <div className="text-body-sm text-on-surface-variant flex items-center gap-4">
        <div>
          Showing <span className="font-medium text-on-surface">1</span> to{" "}
          <span className="font-medium text-on-surface">3</span> of{" "}
          <span className="font-medium text-on-surface">3</span> rooms
        </div>
        <div className="hidden sm:flex items-center gap-2">
          <span>Rows per page:</span>
          <select className="bg-surface border border-outline-variant rounded px-2 py-1 text-body-sm focus:outline-none focus:ring-1 focus:ring-primary">
            <option>10</option>
            <option>25</option>
            <option>50</option>
          </select>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <button
          disabled
          className="p-1.5 rounded-lg border border-outline-variant text-on-surface-variant disabled:opacity-50 disabled:cursor-not-allowed hover:bg-surface-container-low transition-colors"
        >
          <ChevronLeft className="w-5 h-5" />
        </button>
        <button className="w-8 h-8 rounded-lg bg-primary text-on-primary text-label-md font-medium flex items-center justify-center">
          1
        </button>
        <button
          disabled
          className="p-1.5 rounded-lg border border-outline-variant text-on-surface-variant disabled:opacity-50 disabled:cursor-not-allowed hover:bg-surface-container-low transition-colors"
        >
          <ChevronRight className="w-5 h-5" />
        </button>
      </div>
    </div>
  );
};
