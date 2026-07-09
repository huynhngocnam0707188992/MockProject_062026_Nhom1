import { useState, useEffect, useCallback } from "react";
import { roomsApi } from "@/services/rooms-api";
import type { Room } from "@/services/rooms-api";

export function useRooms(facilityId?: string) {
  const [rooms, setRooms] = useState<Room[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchRooms = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await roomsApi.getRooms(facilityId);
      setRooms(data);
      setError(null);
    } catch (err: any) {
      setError(err.message || "Failed to fetch rooms");
    } finally {
      setIsLoading(false);
    }
  }, [facilityId]);

  useEffect(() => {
    fetchRooms();
  }, [fetchRooms]);

  const addRoom = async (room: Omit<Room, "id">) => {
    try {
      const newRoom = await roomsApi.createRoom(room);
      setRooms((prev) => [...prev, newRoom]);
      return newRoom;
    } catch (err: any) {
      throw new Error(err.message || "Failed to add room");
    }
  };

  const editRoom = async (id: string, updates: Partial<Room>) => {
    try {
      const updatedRoom = await roomsApi.updateRoom(id, updates);
      setRooms((prev) =>
        prev.map((r) => (r.id === id ? updatedRoom : r))
      );
      return updatedRoom;
    } catch (err: any) {
      throw new Error(err.message || "Failed to update room");
    }
  };

  return { rooms, isLoading, error, fetchRooms, addRoom, editRoom };
}
