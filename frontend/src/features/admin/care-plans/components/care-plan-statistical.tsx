import { AlarmClock, Clock, Database, Paperclip } from "lucide-react";
import Card from "../ui/card";
import type { CarePlan } from "@/services/care-plan/care-plan-types";

type CarePlanStatisticalProps = {
  carePlans: CarePlan[];
};

export default function CarePlanStatistical(props: CarePlanStatisticalProps) {
  const getTotalCarePlan = () => {
    return props.carePlans.length;
  };

  const getTotalDraft = () => {
    return props.carePlans.filter((carePlan) => carePlan.status === "DRAFT")
      .length;
  };

  const getTotalActive = () => {
    return props.carePlans.filter((carePlan) => carePlan.status === "ACTIVE")
      .length;
  };

  const getTotalDiscontinue = () => {
    return props.carePlans.filter(
      (carePlan) => carePlan.status === "DISCONTINUED",
    ).length;
  };

  return (
    <div className="flex gap-3 flex-wrap">
      <Card
        icon={Database}
        title="Total plans"
        amount={getTotalCarePlan().toString()}
        className="bg-blue-200"
        width="basis-[calc((100%-36px)/4)]"
        height="h-[120px]"
      ></Card>

      <Card
        icon={AlarmClock}
        title="Active"
        amount={getTotalActive().toString()}
        className="bg-blue-200"
        width="basis-[calc((100%-36px)/4)]"
        height="h-[120px]"
      ></Card>
      <Card
        icon={Clock}
        title="Draft"
        amount={getTotalDraft().toString()}
        width="basis-[calc((100%-36px)/4)]"
        className="bg-blue-200"
        height="h-[120px]"
      ></Card>
      <Card
        icon={Paperclip}
        title="Discontinued"
        amount={getTotalDiscontinue().toString()}
        className="bg-blue-200"
        height="h-[120px]"
        width="basis-[calc((100%-36px)/4)]"
      ></Card>
    </div>
  );
}
