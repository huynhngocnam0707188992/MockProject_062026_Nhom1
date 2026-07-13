import React from "react";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight } from "lucide-react";

export function Pagination() {
  return (
    <div className="flex flex-col items-center justify-between gap-4 border-t border-gray-200 px-6 py-4 sm:flex-row">
      <p className="text-sm text-slate-500">
        Showing <span className="font-semibold">1</span> to <span className="font-semibold">5</span> of <span className="font-semibold">24</span> results
      </p>

      <div className="flex items-center gap-2">
        <Button variant="outline" size="icon" className="h-9 w-9">
          <ChevronLeft className="size-4" />
        </Button>

        <Button variant="default" className="h-9 w-9 rounded-lg bg-blue-600 p-0 text-white hover:bg-blue-700">1</Button>

        <Button variant="outline" className="h-9 w-9 rounded-lg p-0">2</Button>

        <Button variant="outline" className="h-9 w-9 rounded-lg p-0">3</Button>

        <span className="px-2 text-slate-400">...</span>

        <Button variant="outline" className="h-9 w-9 rounded-lg p-0">5</Button>

        <Button variant="outline" size="icon" className="h-9 w-9">
          <ChevronRight className="size-4" />
        </Button>
      </div>
    </div>
  );
}
