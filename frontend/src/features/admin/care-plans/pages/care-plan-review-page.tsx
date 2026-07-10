import CarePlanReviewComplianceChecklist from "../components/care-plan-review/care-plan-review-compliance-checklist";
import CarePlanReviewAuthor from "../components/care-plan-review/care-plan-review-author";
import CarePlanReviewRejection from "../components/care-plan-review/care-plan-review-rejection";
import CarePlanReviewPlanSummary from "../components/care-plan-review/care-plan-review-summary";
import CarePlanReviewTitle from "../components/care-plan-review/care-plan-review-title";
import Text from "../ui/Text";
import CarePlanReviewDecision from "../components/care-plan-review/care-plan-review-decision";
import CarePlanReviewIDT from "../components/care-plan-review/care-plan-review-idt";
import Flag from "../ui/flag";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";

export default function CarePlanReviewPage() {
  return (
    <div className="p-8  bg-gray-200">
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans">
              Care Planning
            </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans/review">
              Review
            </BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbLink href="/admin/care-plans/review">
              Name
            </BreadcrumbLink>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb>
      <div className="flex flex-row items-center gap-3">
        <CarePlanReviewTitle residentName="Leesin"></CarePlanReviewTitle>
        <Flag title="Pending Review"></Flag>
      </div>
      <div className="grid grid-cols-[60%_40%] gap-3">
        <div className="flex flex-col">
          <CarePlanReviewPlanSummary></CarePlanReviewPlanSummary>
          <CarePlanReviewAuthor className="mt-[16px]"></CarePlanReviewAuthor>
          <CarePlanReviewRejection className="mt-[16px]"></CarePlanReviewRejection>
        </div>
        <div className="">
          <div className="flex flex-col">
            <CarePlanReviewComplianceChecklist></CarePlanReviewComplianceChecklist>
            <CarePlanReviewIDT className="mt-[16px]"></CarePlanReviewIDT>
            <CarePlanReviewDecision className="mt-[16px]"></CarePlanReviewDecision>
          </div>
        </div>
      </div>
    </div>
  );
}
