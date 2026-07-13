import { apiClient } from "@/lib/api-client";

export interface CareTask {
  id: string;
  residentName: string;
  room: string;
  residentImageUrl: string;
  taskType: string;
  taskTypeIcon: string;
  goal: string;
  time: string;
  status: "Done" | "Pending" | "Missed";
  isAbnormal: boolean;
}

export interface CnaGroup {
  id: string;
  name: string;
  role: string;
  imageUrl: string;
  imageAlt: string;
  totalTasks: number;
  completedTasks: number;
  missedTasks?: number;
  tasks: CareTask[];
}

// ── By-Resident types ────────────────────────────────────────────────────────

export interface ResidentTask {
  id: string;
  time: string;
  taskType: string;
  taskTypeIcon: string;
  goalTitle: string;
  goalDetail: string;
  hasFlag?: boolean;
  flagNote?: string;
  assignedCnaName: string;
  assignedCnaImageUrl?: string;
  status: "Done" | "Pending" | "Missed";
}

export type ResidentCareLevel =
  | "Standard Care"
  | "High Fall Risk"
  | "Memory Care"
  | "Palliative";

export interface ResidentGroup {
  id: string;
  name: string;
  age: number;
  room: string;
  imageUrl: string;
  careLevel: ResidentCareLevel;
  /** Green dot = active, Yellow = fall-risk, Red = alert */
  statusDot: "active" | "fall-risk" | "alert";
  tasks: ResidentTask[];
}

// Mock Data
let mockCnaGroups: CnaGroup[] = [
  {
    id: 'cna-1',
    name: 'Sarah G.',
    role: 'CERTIFIED NURSING ASSISTANT',
    imageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuDpthW2B_bkcTWh6oKSyU6u2Rfsj2PwkIFT3Uo4E2wzGzhi3gcITE78J7Znbj33BWUX9I7wTQuDcaMiZ2ItxsgpsrqUpN-iMnbpEvrfBeM9kaKERhAKlqYf3a6Q2HbuKx9tsjXSRabKCei0iu_Gcgm5VPJy2W7frAUAmAz1YZ_uTF2WHclZWPlnn99zjtHOQS0VBGcejWl3lVhv_6PaaAEjuS5GliiCc8E7bnUImyA96CvZA5q3_kF5',
    imageAlt: 'Close up portrait photo of Sarah G',
    totalTasks: 7,
    completedTasks: 3,
    tasks: [
      {
        id: 'task-1',
        residentName: 'John Doe',
        room: '101-B',
        residentImageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuDml1OLCgnRwBkvBKdtSLk_HyBbyg7c6LtIy0_KVL8Maxcn2V2-1WhugWZFsrB2KjCQx5KnMeCfRTGQtV4hpRnq5XbqApFay5SeJNGGjMCmNS9c2m4HUcBOcQNDsa6vbM5sYo15o_rg7PNAobzq802MvHonF3mRlXBJSB-e2_PUfZOskzz5Nk-hLpLbLZiw9NKRvTVg5D7ZPPkw3jKFqrDJQnht0as7-k_yCVMwIGubgtT9EECQIETU',
        taskType: 'Bathing',
        taskTypeIcon: 'shower',
        goal: 'Maintain daily hygiene and check skin integrity.',
        time: '08:00 AM',
        status: 'Pending',
        isAbnormal: false,
      },
      {
        id: 'task-2',
        residentName: 'Elena Ramos',
        room: '106-A',
        residentImageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuCcVe9-Rq-B9FoUynxbQLXQ2HssUDP2OVBinWk0X4tSEvhq4Rk4QvELNJLLnZzcCjtOn65SDzhTKrctVWgGO6I90FDC3V9vXo-kPwbmenXn_kAQ9pBYIagur2xoO2gi6wKKcBk-n1m7iCZTlmYUVnV-ilDf1sQ4LeuH_pgx7hH6BIZCAemjeKdl73AyQs-NN_k3B-gjOvuS6aE565_2Dd4wNM32ki0uAczzEYeOOStbdFt3DUk2Rg4L',
        taskType: 'Medication',
        taskTypeIcon: 'medication',
        goal: 'Administer morning dosage with food.',
        time: '09:30 AM',
        status: 'Done',
        isAbnormal: false,
      },
    ],
  },
  {
    id: 'cna-2',
    name: 'Mark J.',
    role: 'CERTIFIED NURSING ASSISTANT',
    imageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuDeZduXQVSi4pf98Huu6AQvrpNNiF1um5lw2akK1zdUrBlODT24639mD28DyKVI4eQdqdg9zIaSlOIvdZVAP8bWo9ZW88Z0vkSSJRsZpm4qzNtLkGOROrMdnr0UCd8b4nZxnK46gBeeCw2cUVa_lCXXDd1jtMdAW_vpIGdUtRrjJuDideYlLPXobwM5RUV62o2PdYkPKGpftMCXptBfXnrQmKAq-CmnH_u3dxDsOFAahBwwcjgYJr6V',
    imageAlt: 'Portrait of Mark J',
    totalTasks: 10,
    completedTasks: 8,
    missedTasks: 1,
    tasks: [
      {
        id: 'task-3',
        residentName: 'Susan Wright',
        room: '114-B',
        residentImageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuC_-TY-CZQvUDzFQbdW1n6H9JlwI2NDgZom3nwOPJ47oAtFuXlH3RJDfJ-r3FmkjZcJaM9JvoVVaHOmcp5UoOLR-RMYilXoE9iXcEFJzuCvtR-47FVdLdzh8ySW5oIR26tHSfU56O8nGXEWpearwksa4eagljnMwWBjaElIhlMHxOtEpnYV-nUZdJWUttG3-C75sflzShycozytZc6sj3Q8N39kMYh-IqoAGT4DgBptAq6uzRZUAW-U',
        taskType: 'Meals',
        taskTypeIcon: 'restaurant',
        goal: 'Assist with breakfast. Monitor intake.',
        time: '07:30 AM',
        status: 'Missed',
        isAbnormal: true,
      },
    ],
  },
];

export const careTasksApi = {
  getTasksByDate: async (_date?: string): Promise<CnaGroup[]> => {
    // TODO: Switch to real API when backend is ready
    // const { data } = await apiClient.get("/admin/care-tasks", { params: { date } });
    // return data.data || data;
    
    // Mock network delay
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([...mockCnaGroups]);
      }, 500);
    });
  },

  getTasksByResident: async (_date?: string): Promise<ResidentGroup[]> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([...mockResidentGroups]);
      }, 500);
    });
  },

  completeTask: async (taskId: string): Promise<void> => {
    // TODO: Switch to real API
    // await apiClient.patch(`/admin/care-tasks/${taskId}/complete`);
    
    return new Promise((resolve) => {
      setTimeout(() => {
        // Update mock state so UI reflects the change
        mockCnaGroups = mockCnaGroups.map(group => {
          let updated = false;
          const newTasks = group.tasks.map(task => {
            if (task.id === taskId && task.status !== "Done") {
              updated = true;
              return { ...task, status: "Done" as const };
            }
            return task;
          });
          
          if (updated) {
            return {
              ...group,
              tasks: newTasks,
              completedTasks: group.completedTasks + 1,
              missedTasks: group.missedTasks ? group.missedTasks - 1 : 0
            };
          }
          return group;
        });
        resolve();
      }, 300);
    });
  },

  rescheduleTask: async (taskId: string): Promise<void> => {
    // TODO: Switch to real API
    // await apiClient.patch(`/admin/care-tasks/${taskId}/reschedule`);
    
    return new Promise((resolve) => {
      setTimeout(() => {
        mockCnaGroups = mockCnaGroups.map(group => {
          let updated = false;
          const newTasks = group.tasks.map(task => {
            if (task.id === taskId) {
              updated = true;
              return { ...task, status: "Pending" as const };
            }
            return task;
          });
          
          if (updated) {
            return {
              ...group,
              tasks: newTasks,
              missedTasks: group.missedTasks ? group.missedTasks - 1 : 0
            };
          }
          return group;
        });
        
        // Also update mockResidentGroups
        mockResidentGroups = mockResidentGroups.map(group => {
          let updated = false;
          const newTasks = group.tasks.map(task => {
            if (task.id === taskId) {
              updated = true;
              return { ...task, status: "Pending" as const };
            }
            return task;
          });
          if (updated) return { ...group, tasks: newTasks };
          return group;
        });
        
        resolve();
      }, 300);
    });
  },
};

// ── Mock By Resident Data ──────────────────────────────────────────────────

let mockResidentGroups: ResidentGroup[] = [
  {
    id: "res-1",
    name: "John Doe",
    age: 82,
    room: "101-B",
    careLevel: "Standard Care",
    statusDot: "active",
    imageUrl:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuAb5jmImGeqW0Vs7jYdO11SIOGbqrRRpdmZALbrZ0_IlM0n5e-5_deYwHcXZ7bi1bAa5jML-EE6GhA7FtLimXAkCEkZXdloI8wlJEd6P61TKdVuHzrP24HOBe2epSz72xt8UWFwZNiW9xdI8uNsVPZRJ7LeD7ex1yt3RS7GRWlpDuY973bmIB4QA9NNuajw4JYggT8jpfWhWkJFYxqX-w3dIBb8lQQrQ2vZ82w6KqpAsqM1rGFuHYZY",
    tasks: [
      {
        id: "task-101",
        time: "08:00 AM",
        taskType: "Bathing",
        taskTypeIcon: "shower",
        goalTitle: "Maintain hygiene",
        goalDetail: "Assist with shower and dressing.",
        assignedCnaName: "Sarah G.",
        assignedCnaImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuDoU-3Ij1Q2v-yNQCBmT7UKAHywbaSnYh2Pqj8Gvt1jyiIkZM0Jmi9bP6PzmHCugv2I3LPgY3hlnnejYmYAq2MMXocuF0IUZVEUAX9NQ90aKdf1B_2ExhQVhhCRy4-1Hi0WURxLeoWPtgrLcXThcfKicDoJ4ukmAVSfuBNdsHFmGVxcRfA3DFW2i4CTiK-n7occpI--oxoVo0Fah76bmdYsUZIix3NlXNhidJ3kwm6zvRatobcb2xvV",
        status: "Pending",
      },
      {
        id: "task-102",
        time: "09:00 AM",
        taskType: "Medication",
        taskTypeIcon: "medication",
        goalTitle: "Morning meds",
        goalDetail: "Lisinopril 10mg, Aspirin 81mg.",
        assignedCnaName: "Nurse Carter",
        assignedCnaImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuDNlJmqo-W1XkYeuQVCbCrDIPAuqKy_zdFIP8209jByxB2UWNaF0jnW5NuFQZwsycYw9S5gPbUH9Qmxa6UpGFSIQQ7CinjpJYZ2Gecbsue-P7cNc6wMzSBX_wmKOVWfNlDYqCeiyZEg-q4DvYdGVNQo8v4F26WN9GUdghqIDdoHZhSgAEgYFmoNwB8WeDdbqDITrVxzV2o3njSTyTCgLoR1_jG_PjClTRxZ1f81e5I9ku8NbZDIZHzX",
        status: "Done",
      },
      {
        id: "task-103",
        time: "12:30 PM",
        taskType: "Nutrition",
        taskTypeIcon: "restaurant",
        goalTitle: "Lunch Assistance",
        goalDetail: "Low sodium diet. Monitor fluid intake.",
        hasFlag: true,
        flagNote: "Dietary restriction noted",
        assignedCnaName: "Unassigned",
        status: "Pending",
      }
    ]
  },
  {
    id: "res-2",
    name: "Elena Ramos",
    age: 78,
    room: "106-A",
    careLevel: "High Fall Risk",
    statusDot: "fall-risk",
    imageUrl:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuBWvqSsqIslHngDBTWsfvvqACrY6NkXeuPmnJfO1yxis6QbLcli8KqIcE4MjeaXzcoA3cpfqdOmpcU67OI3HBkL21WjO-6jeUVw9_LIgEUIPwLCUtKr5N_-hQmniLm7Pzs4dBGyavyCMdr8LTuw2lCQCFE68CTHYglt4adQ0gXnQ9qw0MxagD2cvfOT9i-Jr_8zpvlie2cv0n3eiCXaM5i2hEqtYmJ4GA0eO4I5wp9UMruOtWQ0p7Rc",
    tasks: [
      {
        id: "task-104",
        time: "10:00 AM",
        taskType: "Mobility",
        taskTypeIcon: "directions_walk",
        goalTitle: "Morning walk",
        goalDetail: "Assist walk down corridor C. Use gait belt.",
        assignedCnaName: "Pham Van Duc",
        assignedCnaImageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuBlgi2ncCELNXmTPsO6uIviUQqbl9_HXalMWDSpw-tugDrL-2SRJufAV41rjJceI3GbfRR2x7bXf6jmIp9s-LA9FiF8vCUrZCxOrHjuw1a8_2c6SQ-BkjgWMLZUV-O7t8vFERKLYKi-Md4DyxN-v2gJh-1qcv60VjmjE4UH8lJKDASjl-zp5z2uCfdouljaJt40AUQyXrZjBqjXROjbdX71vUv1SwMn6fd2rX2pFfi_2CyeUrkvNmm_",
        status: "Pending",
      }
    ]
  }
];
