import { useState, useMemo } from "react";
import { useRooms } from "../hooks/useRooms";
import { useFacilities } from "../hooks/useFacilities";
import type { Room } from "@/services/rooms-api";
import { FacilitySelector } from "../components/rooms-bed-tab/facility-selector";
import { RoomToolbar } from "../components/rooms-bed-tab/room-toolbar";
import { RoomsTable } from "../components/rooms-bed-tab/rooms-table";
import { Pagination } from "../components/rooms-bed-tab/pagination";
import { FormModal } from "@/components/common/form-modal";
import { RoomForm } from "../components/rooms-bed-tab/room-form";

export const RoomsBedTab = () => {
  const [selectedFacility, setSelectedFacility] = useState<string>("all");
  const { rooms, isLoading: isRoomsLoading, addRoom, editRoom } = useRooms(selectedFacility);
  const { facilities, isLoading: isFacilitiesLoading } = useFacilities();
  
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingRoom, setEditingRoom] = useState<Room | undefined>(undefined);

  const filteredRooms = useMemo(() => {
    return rooms.filter((r) =>
      r.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.facilityName.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [rooms, searchTerm]);

  const handleAddClick = () => {
    setEditingRoom(undefined);
    setIsModalOpen(true);
  };

  const handleEditClick = (room: Room) => {
    setEditingRoom(room);
    setIsModalOpen(true);
  };

  const handleSubmit = async (data: any) => {
    // Determine facility name based on selected ID
    const facility = facilities.find(f => f.id === data.facilityId);
    const roomData = {
      ...data,
      facilityName: facility ? facility.name : "Unknown",
      status: "Available", // Simplified status logic for mock
      beds: Array.from({ length: data.capacity }).map((_, i) => ({
        id: `new-bed-${Date.now()}-${i}`,
        name: `Bed ${String.fromCharCode(65 + i)}`,
        status: "Available"
      }))
    };

    if (editingRoom) {
      await editRoom(editingRoom.id, { ...data, facilityName: roomData.facilityName });
    } else {
      await addRoom(roomData);
    }
    setIsModalOpen(false);
  };

  if (isRoomsLoading || isFacilitiesLoading) {
    return <div className="p-8 text-center text-on-surface-variant">Loading rooms data...</div>;
  }

  return (
    <div className="flex-1 flex flex-col h-full">
      <FacilitySelector 
        facilities={facilities} 
        selectedFacility={selectedFacility} 
        onFacilityChange={setSelectedFacility} 
      />

      <div className="bg-surface-container-lowest rounded-xl border border-outline-variant shadow-sm overflow-hidden flex flex-col mb-6">
        <RoomToolbar 
          onSearch={setSearchTerm} 
          onAddRoom={handleAddClick} 
          totalRooms={rooms.length} 
        />
        <RoomsTable rooms={filteredRooms} onEdit={handleEditClick} />
        <Pagination />
      </div>

      <FormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingRoom ? "Edit Room" : "Add New Room"}
      >
        <RoomForm
          initialData={editingRoom}
          facilities={facilities}
          onSubmit={handleSubmit}
          onCancel={() => setIsModalOpen(false)}
        />
      </FormModal>
    </div>
  );
};