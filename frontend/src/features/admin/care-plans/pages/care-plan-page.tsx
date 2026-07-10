import CarePlanOptions from "../components/care-plan-options";
import CarePlanTitle from "../components/care-plan-title";

import CarePlanStatistical from "../components/care-plan-statistical";
import CarePlanTable from "../components/care-plan-table";
const CarePlanPage = () => {
  return (
    <div>
      <CarePlanTitle></CarePlanTitle>
      <CarePlanOptions></CarePlanOptions>
      <CarePlanStatistical></CarePlanStatistical>
      <CarePlanTable></CarePlanTable>
    </div>
  );
};

export default CarePlanPage;
