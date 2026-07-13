import React from "react";
import Text from "../../ui/Text";
import Title from "../../ui/Title";
import Flag from "../../ui/flag";

export default function CarePlanReviewComplianceChecklist() {
  return (
    <div className="bg-[#fafcfe] border-2 border-solid border-gray-200 rounded-[8px] p-[8px]">
      <Title className="text-[16px] mb-[6px]">Compliance Checklist</Title>
      <Text>All items required to approve</Text>
      <div>
        <div className="flex flex-row gap-3 mt-[4px] mb-[4px]">
          <input type="checkbox" name="" id="" />

          <Text>option</Text>
        </div>

        <div className="flex flex-row gap-3 mt-[4px] mb-[4px]">
          <input type="checkbox" name="" id="" />
          <Text>option</Text>
        </div>

        <div className="flex flex-row gap-3 mt-[4px] mb-[4px]">
          <input type="checkbox" name="" id="" />
          <Text>option</Text>
        </div>
        <div className="flex flex-row gap-3 mt-[4px] mb-[4px]">
          <input type="checkbox" name="" id="" />

          <Text>option</Text>
        </div>
      </div>
      <div>
        <Flag className="mt-[4px] mb-[4px]" title="4/4 Completed"></Flag>
      </div>
    </div>
  );
}
