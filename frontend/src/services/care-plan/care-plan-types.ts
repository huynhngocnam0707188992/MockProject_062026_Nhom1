export type CarePlan = {
  id: number;
  residentId: number;
  status: "DRAFT" | "ACTIVE" | "RESOLVED" | "DISCONTINUED";
  significantFlag: boolean;
  goalCount: number;
  interventionCount: number;
  createdAt: string;
  updatedAt: string;
  isDeleted: boolean;
};

export type GetCarePlanListResponse = {
  statusCode: number;
  message: string;
  data: {
    list: CarePlan[];
  };
};
