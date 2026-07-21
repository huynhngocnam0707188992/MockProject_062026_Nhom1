import { cn } from "@/lib/utils";
import Text from "../../ui/Text";
import Title from "../../ui/Title";

type CarePlanReviewAuthorProps = {
  className?: string;
};
function CarePlanReviewAuthor({ className }: CarePlanReviewAuthorProps) {
  return (
    <div
      className={cn(
        `bg-[#fafcfe] border-2 border-solid border-gray-200 rounded-[8px] p-[8px]`,
        className,
      )}
    >
      <Title className="text-[16px] mb-[6px]">Author Accountability</Title>
      <div id="interventions">
        <Text>Prepared by: asjkldjaslkd</Text>
        <Text>Prepared on: lsdkajflkasdjflk</Text>
      </div>
    </div>
  );
}

export default CarePlanReviewAuthor;
