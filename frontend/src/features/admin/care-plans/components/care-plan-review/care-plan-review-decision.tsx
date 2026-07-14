import { Button } from "@/components/ui/button";
import Title from "../../ui/Title";
import Text from "../../ui/Text";
import { cn } from "@/lib/utils";
import ApproveCarePlanDialog from "./approve-care-plan-dialog";

type CarePlanReviewDecisionProps = {
  className?: string;
};
export default function CarePlanReviewDecision({
  className,
}: CarePlanReviewDecisionProps) {
  return (
    <div
      className={cn(
        `bg-[#fafcfe] border-2 border-solid border-gray-200 rounded-[8px] p-[8px]`,
        className,
      )}
    >
      <Title className="text-[16px] mb-[6px">Decision</Title>
      <div className="flex flex-col">
        <Button className={`min-h-14`}>
          <Text className="font-bold text-white">Approve & e-Sign</Text>
        </Button>

        <ApproveCarePlanDialog></ApproveCarePlanDialog>
      </div>
    </div>
  );
}
