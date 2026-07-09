export type FacilityStatus = "Active" | "Inactive" | "Maintenance";

export interface Facility {
  id: string;
  code: string;
  name: string;
  licenseNumber: string;
  state: string;
  phone: string;
  status: FacilityStatus;
}

let mockFacilities: Facility[] = [
  {
    id: "1",
    code: "NHMS-CA-01",
    name: "Golden Years Care Center",
    licenseNumber: "CA-SNF-000123",
    state: "California",
    phone: "916-555-1234",
    status: "Active",
  },
  {
    id: "2",
    code: "NHMS-NV-02",
    name: "Desert Rose Assisted Living",
    licenseNumber: "NV-ALF-009876",
    state: "Nevada",
    phone: "702-555-8899",
    status: "Active",
  },
  {
    id: "3",
    code: "NHMS-AZ-03",
    name: "Canyon View Rehab",
    licenseNumber: "AZ-REH-004561",
    state: "Arizona",
    phone: "602-555-3321",
    status: "Inactive",
  },
];

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const facilitiesApi = {
  getFacilities: async (): Promise<Facility[]> => {
    await delay(300);
    return [...mockFacilities];
  },
  createFacility: async (facility: Omit<Facility, "id">): Promise<Facility> => {
    await delay(500);
    const newFacility = { ...facility, id: String(Date.now()) };
    mockFacilities = [...mockFacilities, newFacility];
    return newFacility;
  },
  updateFacility: async (id: string, updates: Partial<Facility>): Promise<Facility> => {
    await delay(500);
    const index = mockFacilities.findIndex((f) => f.id === id);
    if (index === -1) throw new Error("Facility not found");
    const updatedFacility = { ...mockFacilities[index], ...updates };
    mockFacilities[index] = updatedFacility;
    return updatedFacility;
  },
};
