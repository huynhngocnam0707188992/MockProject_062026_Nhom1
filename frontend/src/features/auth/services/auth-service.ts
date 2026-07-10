import {
  mockUsersByRole,
  CURRENT_MOCK_ROLE,
  type MockUser,
} from "@/mocks/permission-data";

export const fetchCurrentUser = async (): Promise<MockUser> => {
  return mockUsersByRole[CURRENT_MOCK_ROLE];
};
