export const CareTaskTableHeader = () => {
  return (
    <div className="hidden sm:grid grid-cols-12 gap-4 px-gutter py-3 bg-surface-container-low font-label-bold text-label-bold text-on-surface-variant uppercase tracking-wider">
      <div className="col-span-3">Resident</div>
      <div className="col-span-2">Task Type</div>
      <div className="col-span-2">Goal</div>
      <div className="col-span-1 text-center">Time</div>
      <div className="col-span-1 text-center">Status</div>
      <div className="col-span-1 text-center">Flags</div>
      <div className="col-span-2 text-right">Actions</div>
    </div>
  );
};
