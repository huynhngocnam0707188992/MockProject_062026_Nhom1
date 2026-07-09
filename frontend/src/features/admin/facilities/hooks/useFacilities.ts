import { useState, useEffect, useCallback } from "react";
import { facilitiesApi } from "@/services/facilities-api";
import type { Facility } from "@/services/facilities-api";

export function useFacilities() {
  const [facilities, setFacilities] = useState<Facility[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchFacilities = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await facilitiesApi.getFacilities();
      setFacilities(data);
      setError(null);
    } catch (err: any) {
      setError(err.message || "Failed to fetch facilities");
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchFacilities();
  }, [fetchFacilities]);

  const addFacility = async (facility: Omit<Facility, "id">) => {
    try {
      const newFacility = await facilitiesApi.createFacility(facility);
      setFacilities((prev) => [...prev, newFacility]);
      return newFacility;
    } catch (err: any) {
      throw new Error(err.message || "Failed to add facility");
    }
  };

  const editFacility = async (id: string, updates: Partial<Facility>) => {
    try {
      const updatedFacility = await facilitiesApi.updateFacility(id, updates);
      setFacilities((prev) =>
        prev.map((f) => (f.id === id ? updatedFacility : f))
      );
      return updatedFacility;
    } catch (err: any) {
      throw new Error(err.message || "Failed to update facility");
    }
  };

  return { facilities, isLoading, error, fetchFacilities, addFacility, editFacility };
}
