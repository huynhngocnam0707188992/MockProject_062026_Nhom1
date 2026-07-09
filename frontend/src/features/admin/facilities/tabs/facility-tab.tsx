import { useState, useMemo } from "react";
import { useFacilities } from "../hooks/useFacilities";
import type { Facility } from "@/services/facilities-api";
import { FacilityToolbar } from "../components/facilities-tab/facility-toolbar";
import { FacilityTable } from "../components/facilities-tab/facility-table";
import { FacilityPagination } from "../components/facilities-tab/facility-pagination";
import { FormModal } from "@/components/common/form-modal";
import { FacilityForm } from "../components/facilities-tab/facility-form";

export const FacilityTab = () => {
  const { facilities, isLoading, addFacility, editFacility } = useFacilities();
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingFacility, setEditingFacility] = useState<Facility | undefined>(undefined);

  const filteredFacilities = useMemo(() => {
    return facilities.filter((f) =>
      f.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      f.code.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [facilities, searchTerm]);

  const handleAddClick = () => {
    setEditingFacility(undefined);
    setIsModalOpen(true);
  };

  const handleEditClick = (facility: Facility) => {
    setEditingFacility(facility);
    setIsModalOpen(true);
  };

  const handleSubmit = async (data: any) => {
    if (editingFacility) {
      await editFacility(editingFacility.id, data);
    } else {
      await addFacility(data);
    }
    setIsModalOpen(false);
  };

  if (isLoading) {
    return <div className="p-8 text-center text-on-surface-variant">Loading facilities...</div>;
  }

  const activeCount = facilities.filter(f => f.status === "Active").length;

  return (
    <div className="flex-1 flex flex-col h-full">
      <FacilityToolbar
        onSearch={setSearchTerm}
        onAddFacility={handleAddClick}
        activeCount={activeCount}
      />
      
      <div className="bg-surface-container-lowest rounded-xl border border-outline-variant shadow-sm overflow-hidden flex flex-col mb-6 w-full">
        <FacilityTable facilities={filteredFacilities} onEdit={handleEditClick} />
        <FacilityPagination />
      </div>

      <FormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingFacility ? "Edit Facility" : "Add New Facility"}
      >
        <FacilityForm
          initialData={editingFacility}
          onSubmit={handleSubmit}
          onCancel={() => setIsModalOpen(false)}
        />
      </FormModal>
    </div>
  );
};

