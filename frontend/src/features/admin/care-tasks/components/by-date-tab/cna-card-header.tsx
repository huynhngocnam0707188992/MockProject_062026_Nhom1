interface CnaCardHeaderProps {
  name: string;
  role: string;
  imageUrl: string;
  imageAlt: string;
  totalTasks: number;
  completedTasks: number;
  missedTasks?: number;
}

export const CnaCardHeader = ({
  name,
  role,
  imageUrl,
  imageAlt,
  totalTasks,
  completedTasks,
  missedTasks,
}: CnaCardHeaderProps) => {
  return (
    <div className="bg-surface-container-high px-gutter py-stack-md flex items-center justify-between">
      <div className="flex items-center gap-stack-md">
        <div className="w-12 h-12 rounded-full overflow-hidden bg-primary-container flex-shrink-0 shadow-sm relative">
          <img
            className="w-full h-full object-cover"
            alt={imageAlt}
            src={imageUrl}
          />
        </div>
        <div>
          <h2 className="font-headline-md text-headline-md text-on-surface">{name}</h2>
          <p className="font-label-md text-label-md text-on-surface-variant mt-0.5 tracking-wide">
            {role}
          </p>
        </div>
      </div>
      <div className="flex items-center gap-3 bg-surface-container-lowest px-4 py-2 rounded-xl shadow-sm">
        <div className="relative w-8 h-8 flex items-center justify-center">
          <svg className="w-full h-full -rotate-90" viewBox="0 0 36 36">
            <path
              className="text-surface-variant"
              d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              fill="none"
              stroke="currentColor"
              strokeWidth="3"
            ></path>
            <path
              className="text-primary"
              d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              fill="none"
              stroke="currentColor"
              strokeDasharray={`${(completedTasks / totalTasks) * 100}, 100`}
              strokeLinecap="round"
              strokeWidth="3"
            ></path>
          </svg>
          <span className="absolute font-label-bold text-[10px] text-on-surface">
            {completedTasks}/{totalTasks}
          </span>
        </div>
        <div className="flex flex-col">
          <span className="font-label-bold text-label-bold text-on-surface">
            {totalTasks} Tasks Today
          </span>
          {missedTasks ? (
            <span className="font-body-sm text-[11px] text-error font-medium">
              {missedTasks} Missed Task
            </span>
          ) : (
            <span className="font-body-sm text-[11px] text-on-surface-variant">
              {totalTasks - completedTasks} remaining
            </span>
          )}
        </div>
      </div>
    </div>
  );
};
