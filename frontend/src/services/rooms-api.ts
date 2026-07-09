export type RoomStatus = "Available" | "Occupied" | "Maintenance";

export interface Bed {
  id: string;
  name: string;
  status: RoomStatus;
  resident?: string;
  admissionDate?: string;
}

export interface Room {
  id: string;
  name: string;
  facilityId: string;
  facilityName: string;
  floor: string;
  capacity: number;
  status: string;
  beds: Bed[];
}

let mockRooms: Room[] = [
  {
    id: "1",
    name: "Room 101",
    facilityId: "1",
    facilityName: "Golden Years Care Center",
    floor: "1st Floor",
    capacity: 2,
    status: "1/2 Occupied",
    beds: [
      { id: "b1", name: "Bed A", status: "Occupied", resident: "John Doe", admissionDate: "Jan 12, 2024" },
      { id: "b2", name: "Bed B", status: "Available" }
    ]
  },
  {
    id: "2",
    name: "Room 102",
    facilityId: "1",
    facilityName: "Golden Years Care Center",
    floor: "1st Floor",
    capacity: 1,
    status: "Full",
    beds: [
      { id: "b3", name: "Bed A", status: "Occupied", resident: "Jane Smith", admissionDate: "Feb 05, 2024" }
    ]
  },
  {
    id: "3",
    name: "Room 201",
    facilityId: "2",
    facilityName: "Desert Rose Assisted Living",
    floor: "2nd Floor",
    capacity: 2,
    status: "Available",
    beds: [
      { id: "b4", name: "Bed A", status: "Available" },
      { id: "b5", name: "Bed B", status: "Available" }
    ]
  }
];

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const roomsApi = {
  getRooms: async (facilityId?: string): Promise<Room[]> => {
    await delay(300);
    if (facilityId && facilityId !== "all") {
      return mockRooms.filter(r => r.facilityId === facilityId);
    }
    return [...mockRooms];
  },
  createRoom: async (room: Omit<Room, "id">): Promise<Room> => {
    await delay(500);
    const newRoom = { ...room, id: String(Date.now()) };
    mockRooms = [...mockRooms, newRoom];
    return newRoom;
  },
  updateRoom: async (id: string, updates: Partial<Room>): Promise<Room> => {
    await delay(500);
    const index = mockRooms.findIndex((r) => r.id === id);
    if (index === -1) throw new Error("Room not found");
    const updatedRoom = { ...mockRooms[index], ...updates };
    mockRooms[index] = updatedRoom;
    return updatedRoom;
  },
};
