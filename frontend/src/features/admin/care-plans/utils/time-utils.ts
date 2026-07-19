export function formatOffsetDateTimeToDate(date?: string | null) {
  if (!date) return "—";

  return date.split("T")[0];
}
