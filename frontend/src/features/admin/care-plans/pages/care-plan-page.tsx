import CarePlanOptions from "../components/care-plan-options";
import CarePlanTitle from "../components/care-plan-title";

import CarePlanStatistical from "../components/care-plan-statistical";
import CarePlanTable from "../components/care-plan-table";
import { getCarePlanList } from "@/services/care-plan/care-plan-services";
import { useEffect, useState } from "react";
import type { GetCarePlanListResponse } from "@/services/care-plan/care-plan-types";
const CarePlanPage = () => {
  // const [carePlans, setCarePlans] = useState<CarePlan[]>([]);
  const [carePlanResponse, setCarePlanResponse] =
    useState<GetCarePlanListResponse | null>(null);

  useEffect(() => {
    loadCarePlans();
  }, []);

  const loadCarePlans = async () => {
    try {
      const response = await getCarePlanList();
      setCarePlanResponse(response);
    } catch (error) {
      console.error(error);
    }
  };

  if (!carePlanResponse) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <CarePlanTitle />
      <CarePlanOptions />

      <CarePlanStatistical
        carePlans={carePlanResponse.data.list}
        carePlanMetadata={carePlanResponse.metadata}
      />

      <CarePlanTable carePlans={carePlanResponse.data.list} />
    </div>
  );
};

export default CarePlanPage;
