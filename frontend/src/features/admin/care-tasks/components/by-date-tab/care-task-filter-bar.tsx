export const CareTaskFilterBar = () => {
  return (
    <div className="bg-surface-container-lowest p-stack-sm rounded-xl shadow-sm flex flex-wrap items-center gap-stack-sm mb-6 mt-4">
      <button className="flex items-center gap-2 bg-surface-container hover:bg-surface-container-high px-4 py-2 rounded-lg transition-colors text-on-surface">
        <span className="material-symbols-outlined text-[18px] text-on-surface-variant">
          calendar_today
        </span>
        <span className="font-label-bold text-label-bold">Oct 24, 2026</span>
      </button>
      <div className="w-px h-6 bg-surface-variant mx-1"></div>
      
      <button className="flex items-center gap-2 bg-surface hover:bg-surface-container px-3 py-2 rounded-lg transition-colors text-on-surface-variant">
        <span className="font-body-md text-body-md">Shift:</span>
        <span className="font-label-bold text-label-bold text-on-surface">All</span>
        <span className="material-symbols-outlined text-[16px]">expand_more</span>
      </button>
      
      <button className="flex items-center gap-2 bg-surface hover:bg-surface-container px-3 py-2 rounded-lg transition-colors text-on-surface-variant">
        <span className="font-body-md text-body-md">Status:</span>
        <span className="font-label-bold text-label-bold text-on-surface">All</span>
        <span className="material-symbols-outlined text-[16px]">expand_more</span>
      </button>
      
      <button className="flex items-center gap-2 bg-surface hover:bg-surface-container px-3 py-2 rounded-lg transition-colors text-on-surface-variant">
        <span className="font-body-md text-body-md">CNA:</span>
        <span className="font-label-bold text-label-bold text-on-surface">All CNAs</span>
        <span className="material-symbols-outlined text-[16px]">expand_more</span>
      </button>
      
      <button className="flex items-center gap-2 bg-surface hover:bg-surface-container px-3 py-2 rounded-lg transition-colors text-on-surface-variant">
        <span className="font-body-md text-body-md">Type:</span>
        <span className="font-label-bold text-label-bold text-on-surface">All Types</span>
        <span className="material-symbols-outlined text-[16px]">expand_more</span>
      </button>
      
      <div className="flex-1 min-w-[200px]"></div>
      
      <div className="flex items-center bg-surface-container px-3 py-2 rounded-lg">
        <span className="material-symbols-outlined text-on-surface-variant text-[18px] mr-2">
          search
        </span>
        <input
          className="bg-transparent border-none focus:ring-0 font-body-md text-body-md w-full text-on-surface outline-none placeholder:text-on-surface-variant/70"
          placeholder="Search resident..."
          type="text"
        />
      </div>
    </div>
  );
};
