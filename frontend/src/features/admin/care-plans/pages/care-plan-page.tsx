import CarePlanOptions from "../components/care-plan-options";
import CarePlanTitle from "../components/care-plan-title";

import CarePlanStatistical from "../components/care-plan-statistical";
import CarePlanTable from "../components/care-plan-table";
import { getCarePlanList } from "@/services/care-plan/care-plan-services";
import { useEffect, useState } from "react";
import type { CarePlan } from "@/services/care-plan/care-plan-types";
const CarePlanPage = () => {
  const [carePlans, setCarePlans] = useState<CarePlan[]>([]);

  useEffect(() => {
    loadCarePlans();
  }, []);
  const loadCarePlans = async () => {
    try {
      const data = await getCarePlanList();
      setCarePlans(data);
    } catch (error) {
      console.error(error);
    }
  };
  return (
    <div>
      <CarePlanTitle></CarePlanTitle>
      <CarePlanOptions></CarePlanOptions>
      <CarePlanStatistical carePlans={carePlans}></CarePlanStatistical>
      <CarePlanTable carePlans={carePlans}></CarePlanTable>
    </div>
  );
};

export default CarePlanPage;
