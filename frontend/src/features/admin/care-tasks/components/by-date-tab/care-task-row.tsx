import type { CareTask } from "@/services/care-tasks-api";

interface CareTaskRowProps {
  task: CareTask;
}

export const CareTaskRow = ({ task }: CareTaskRowProps) => {
  const isCompleted = task.status === "Done";
  const isMissed = task.status === "Missed";
  const bgClass = isMissed
    ? "bg-surface-bright"
    : isCompleted
    ? "bg-surface-container-lowest"
    : "bg-surface-bright";
  const indicatorColor = isMissed
    ? "bg-error"
    : isCompleted
    ? "bg-surface-variant opacity-50"
    : "bg-secondary-container";

  return (
    <div
      className={`grid grid-cols-1 sm:grid-cols-12 gap-4 sm:gap-4 px-gutter py-4 items-center ${bgClass} hover:bg-surface-container transition-colors group relative`}
    >
      <div className={`absolute left-0 top-0 bottom-0 w-1 ${indicatorColor}`}></div>

      {/* Resident */}
      <div className="col-span-1 sm:col-span-3 flex items-center gap-3">
        <div
          className={`w-10 h-10 rounded-full overflow-hidden ${
            isMissed
              ? "bg-error-container"
              : isCompleted
              ? "bg-primary-container"
              : "bg-secondary-container"
          } flex-shrink-0`}
        >
          <img
            className="w-full h-full object-cover"
            alt={`Portrait of ${task.residentName}`}
            src={task.residentImageUrl}
          />
        </div>
        <div className="flex flex-col min-w-0">
          <span className="font-headline-md text-[15px] leading-tight text-on-surface truncate">
            {task.residentName}
          </span>
          <span className="font-body-sm text-body-sm text-on-surface-variant flex items-center gap-1 mt-0.5">
            <span className="material-symbols-outlined text-[14px]">meeting_room</span>{" "}
            {task.room}
          </span>
        </div>
      </div>

      {/* Task Type */}
      <div className="col-span-1 sm:col-span-2 flex items-center">
        <span
          className={`${
            isMissed
              ? "bg-surface-container-high text-on-surface"
              : isCompleted
              ? "bg-primary-container text-on-primary-container"
              : "bg-secondary-container text-on-secondary-container"
          } font-label-bold text-label-bold px-2.5 py-1 rounded-md inline-flex items-center gap-1.5`}
        >
          <span className="material-symbols-outlined text-[14px]">{task.taskTypeIcon}</span>{" "}
          {task.taskType}
        </span>
      </div>

      {/* Goal */}
      <div className="col-span-1 sm:col-span-2">
        <p className="font-body-sm text-body-sm text-on-surface-variant line-clamp-2">
          {task.goal}
        </p>
      </div>

      {/* Time */}
      <div className="col-span-1 text-left sm:text-center">
        <span
          className={`font-mono-data text-mono-data ${
            isMissed
              ? "bg-error-container/50 text-error"
              : isCompleted
              ? "bg-surface-container text-on-surface-variant"
              : "bg-surface-container-high text-on-surface"
          } px-2 py-1 rounded`}
        >
          {task.time}
        </span>
      </div>

      {/* Status */}
      <div className="col-span-1 text-left sm:text-center">
        {isCompleted ? (
          <span className="bg-surface-container-high text-on-surface font-label-bold text-[11px] uppercase tracking-wider px-2 py-1 rounded-full flex items-center justify-center gap-1 w-max mx-auto">
            <span className="material-symbols-outlined text-[12px]">done</span> {task.status}
          </span>
        ) : (
          <span
            className={`${
              isMissed
                ? "bg-error-container text-on-error-container"
                : "bg-surface-variant text-on-surface-variant"
            } font-label-bold text-[11px] uppercase tracking-wider px-2 py-1 rounded-full`}
          >
            {task.status}
          </span>
        )}
      </div>

      {/* Flags */}
      <div className="col-span-1 text-left sm:text-center flex justify-start sm:justify-center">
        {task.isAbnormal ? (
          <div
            className="bg-error-container text-error w-8 h-8 rounded-full flex items-center justify-center shadow-sm animate-pulse"
            title="Abnormal findings flagged"
          >
            <span className="material-symbols-outlined text-[18px]">warning</span>
          </div>
        ) : (
          <span
            className={`w-6 h-6 flex items-center justify-center text-outline-variant ${
              !isCompleted ? "group-hover:text-outline transition-colors" : ""
            }`}
            title="No abnormal findings"
          >
            <span className="material-symbols-outlined text-[18px]">check_circle</span>
          </span>
        )}
      </div>

      {/* Actions */}
      <div
        className={`col-span-1 sm:col-span-2 flex items-center justify-start sm:justify-end gap-2 ${
          isCompleted ? "opacity-60" : ""
        }`}
      >
        {isCompleted ? (
          <button className="bg-surface-container-high text-on-surface font-label-bold text-label-bold px-4 py-2 rounded-lg cursor-not-allowed flex-1 sm:flex-none text-center">
            Completed
          </button>
        ) : (
          <>
            <button
              className={`${
                isMissed
                  ? "bg-surface-container text-on-surface hover:bg-surface-variant"
                  : "bg-primary text-on-primary hover:bg-primary/90"
              } font-label-bold text-label-bold px-4 py-2 rounded-lg shadow-sm transition-colors flex-1 sm:flex-none text-center`}
            >
              {isMissed ? "Reschedule" : "Complete"}
            </button>
            <button className="w-9 h-9 flex items-center justify-center bg-surface-container rounded-lg text-on-surface-variant hover:bg-surface-variant transition-colors">
              <span className="material-symbols-outlined text-[18px]">more_vert</span>
            </button>
          </>
        )}
      </div>
    </div>
  );
};
