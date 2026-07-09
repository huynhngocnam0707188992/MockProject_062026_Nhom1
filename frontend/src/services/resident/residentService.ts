import api from '../api'

export interface ResidentListItem {
  id: number
  name: string
  room: string
  status: string
  dob: string
  age: number
  payerSource: string
  referralSource: string
}

export interface ResidentDetail {
  id: number
  name: string
  initials: string
  room: string
  status: string
  dob: string
  age: number
  badges: Array<{ text: string; type: string }>
  demographics: {
    legalName: string
    address: string
    dob: string
    admissionDate: string
    ssn: string
    roomBed: string
    gender: string
    referralSource: string
    maritalStatus: string
    emergencyContact: string
    phone: string
    payerSource: string
  }
  poa: {
    name: string
    relationship: string
    contact: string
    dnrFlag: string
  }
  diagnoses: string[]
  allergies: string[]
  insurance: {
    medicareNum: string
    provider: string
    payerName: string
    authNum: string
    authStartEnd: string
  }
  locSummary: {
    level: string
    adlScore: string
  }
}

export interface ResidentSaveRequest {
  firstName: string
  lastName: string
  status: string
  dob: string
  gender: string
  referralSource: string
  ssn: string
  maritalStatus: string
  referringFacility: string
  phone: string
  address: string
  emergencyContact: string
  emergencyPhone: string
  poaOnFile: boolean
  poaName: string
  poaRelationship: string
  payerSource: string
  payerType: string
  medicareNum: string
  insuranceProvider: string
  authStartDate: string
  authEndDate: string
  dnrActive: boolean
}

export const residentService = {
  getResidents: async (search?: string, status?: string, referral?: string) => {
    const response = await api.get<ResidentListItem[]>('/api/residents', {
      params: { search, status, referral },
    })
    return response.data
  },

  getResidentDetail: async (id: number | string) => {
    const response = await api.get<ResidentDetail>(`/api/residents/${id}`)
    return response.data
  },

  createResident: async (data: ResidentSaveRequest) => {
    await api.post('/api/residents', data)
  },

  updateResident: async (id: number | string, data: ResidentSaveRequest) => {
    await api.put(`/api/residents/${id}`, data)
  },

  deleteResident: async (id: number | string) => {
    await api.delete(`/api/residents/${id}`)
  },
}
