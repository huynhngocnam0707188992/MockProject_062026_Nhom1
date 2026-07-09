import type { ReactNode } from "react";

type SeverityRowProps = {
  level: string;
  badgeClasses: string;
  children: ReactNode;
};

export const SeverityRow = ({
  level,
  badgeClasses,
  children,
}: SeverityRowProps) => (
  <tr className="group hover:bg-surface-container/30 transition-colors">
    <td className="px-6 py-6 align-top">
      <span className={`px-3 py-1 font-label-bold text-sm rounded-full border ${badgeClasses}`}>
        {level}
      </span>
    </td>
    {children}
    <td className="px-6 py-6 text-right align-top">
      <button className="text-primary font-label-bold hover:underline">Edit</button>
    </td>
  </tr>
);
