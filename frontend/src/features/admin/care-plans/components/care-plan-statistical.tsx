import { AlarmClock, Clock, Database, Paperclip } from "lucide-react";
import Card from "../ui/card";
import type {
  CarePlan,
  CarePlanMetadata,
} from "@/services/care-plan/care-plan-types";

type CarePlanStatisticalProps = {
  carePlans: CarePlan[];
  carePlanMetadata: CarePlanMetadata;
};

export default function CarePlanStatistical(props: CarePlanStatisticalProps) {
  const getTotalCarePlan = () => {
    return props.carePlanMetadata.totalElements;
  };

  const getTotalDraft = () => {
    return props.carePlans.filter((carePlan) => carePlan.status === "DRAFT")
      .length;
  };

  const getTotalPendingReview = () => {
    return props.carePlans.filter(
      (carePlan) => carePlan.status === "PENDING_REVIEW",
    ).length;
  };

  const getTotalReviewDue = () => {
    return props.carePlans.filter(
      (carePlan) => carePlan.status === "REVIEW_DUE",
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
        icon={Paperclip}
        title="Draft"
        amount={getTotalDraft().toString()}
        width="basis-[calc((100%-36px)/4)]"
        className="bg-blue-200"
        height="h-[120px]"
      ></Card>
      <Card
        icon={Clock}
        title="Pending Review"
        amount={getTotalPendingReview().toString()}
        width="basis-[calc((100%-36px)/4)]"
        className="bg-blue-200"
        height="h-[120px]"
      ></Card>

      <Card
        icon={AlarmClock}
        title="Pending Review"
        amount={getTotalReviewDue().toString()}
        className="bg-blue-200"
        width="basis-[calc((100%-36px)/4)]"
        height="h-[120px]"
      ></Card>
      <Card></Card>
    </div>
  );
}
