import Text from "../../ui/Text";
import Title from "../../ui/Title";

type CarePlanReviewPlanSummary = {
  goals?: string[];
  interventions?: string[];
};

// function CarePlanReviewPlanSummary({
//   goals,
//   interventions,
// }: CarePlanReviewPlanSummary) {
//   return (
//     <div>
//       <Text>Plan Summary (read-only)</Text>
//       <div id="goals">
//         {goals?.map((item) => {
//           return <Text>{item}</Text>;
//         })}
//       </div>
//       <div id="interventions">
//         {interventions?.map((item) => {
//           return <Text>{item}</Text>;
//         })}
//       </div>
//     </div>
//   );
// }

function CarePlanReviewPlanSummary({
  goals,
  interventions,
}: CarePlanReviewPlanSummary) {
  return (
    <div className="bg-[#fafcfe] border-2 border-solid border-gray-200 rounded-[8px] p-[8px]">
      <Title className="text-[16px] mb-[6px]">Plan Summary (read-only)</Title>
      <div id="goals" className="">
        <Title className="text-[14px] mb-[6px]">Goals</Title>
        <Text>hang 1</Text>
        <Text>hang 1</Text>
        <Text>hang 1</Text>
        <Text>hang 1</Text>
        <Text>hang 1</Text>
        <Text>hang 1</Text>
      </div>
      <div id="interventions">
        <Title className="text-[14px] mb-[6px] mt-[6px]">Interventions</Title>
        <Text> jdskfj 1</Text>
        <Text> jdskfj 1</Text>
        <Text> jdskfj 1</Text>
        <Text> jdskfj 1</Text>
        <Text> jdskfj 1</Text>
      </div>
    </div>
  );
}
export default CarePlanReviewPlanSummary;
