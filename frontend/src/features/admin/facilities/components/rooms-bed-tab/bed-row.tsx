import type { Bed } from "@/services/rooms-api";

interface BedRowProps {
  bed: Bed;
  index: number;
}

export const BedRow = ({ bed, index }: BedRowProps) => {
  return (
    <tr className={`hover:bg-primary/5 transition-colors ${index % 2 !== 0 ? 'bg-surface-bright/30' : ''}`}>
      <td className="p-[12px_16px] pl-12 text-on-surface font-medium">
        <div className="flex items-center gap-2">
          <span className="material-symbols-outlined text-[18px] text-primary">bed</span>
          {bed.name}
        </div>
      </td>
      <td className="p-[12px_16px]">
        {bed.status === "Occupied" ? (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-md text-error font-medium border border-error/20 bg-error/10">
            <span className="w-1.5 h-1.5 rounded-full bg-error"></span>
            Occupied
          </span>
        ) : (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-md text-primary font-medium border border-primary/20 bg-primary/10">
            <span className="w-1.5 h-1.5 rounded-full bg-primary"></span>
            Available
          </span>
        )}
      </td>
      <td className="p-[12px_16px] text-on-surface">
        {bed.resident ? (
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-primary/10 text-primary flex items-center justify-center text-label-md font-bold">
              {bed.resident.split(" ").map(n => n[0]).join("")}
            </div>
            {bed.resident}
          </div>
        ) : (
          <span className="text-on-surface-variant/70 italic">--</span>
        )}
      </td>
      <td className="p-[12px_16px] text-on-surface-variant">
        {bed.admissionDate || <span className="text-on-surface-variant/70 italic">--</span>}
      </td>
      <td className="p-[12px_16px] text-right">
        <button className="p-2 text-on-surface-variant hover:text-primary hover:bg-primary/10 rounded-md transition-colors font-medium" title="Edit Bed">
          Edit
        </button>
      </td>
    </tr>
  );
};
