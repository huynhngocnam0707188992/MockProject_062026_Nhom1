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
  getTasksByDate: async (date?: string): Promise<CnaGroup[]> => {
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
        resolve();
      }, 300);
    });
  },
};
