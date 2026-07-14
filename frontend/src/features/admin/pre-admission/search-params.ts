import { parseAsInteger, parseAsString, useQueryStates } from "nuqs";

export const screeningSearchParams = {
  page: parseAsInteger.withDefault(0),
  size: parseAsInteger.withDefault(10),
  sort: parseAsString.withDefault("createdAt,desc"), // format: "field,direction" e.g. "createdAt,desc"
};

export const useScreeningSearchParams = () => {
  return useQueryStates(screeningSearchParams);
};
