import { ChevronLeft, ChevronRight } from "lucide-react";

export const FacilityPagination = () => {
  return (
    <div className="px-6 py-4 border-t border-outline-variant bg-surface flex items-center justify-between">
      <div className="text-body-sm text-on-surface-variant">
        Showing <span className="font-medium text-on-surface">1</span> to{" "}
        <span className="font-medium text-on-surface">3</span> of{" "}
        <span className="font-medium text-on-surface">3</span> facilities
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
