import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";

import { Button } from "@/components/ui/button";
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
} from "@/components/ui/field";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";

import { useCreateAdmission } from "../hooks/use-create-admission";
import { useCompletedAssessmentsForSelect } from "../../assessment/hooks/use-completed-assessment";
import { useDialogStore } from "@/store/use-dialog-store";
import z from "zod";
import { useFacilityForSelect } from "../hooks/use-facility-select";

const formSchema = z.object({
  assessmentId: z.string().min(1, "Please select an assessment."),
  facilityId: z.string().min(1, "Required."),
  admissionDate: z.string().min(1, "Required."),
});

export const CreateAdmissionForm = () => {
  const { data: assessments, isLoading } = useCompletedAssessmentsForSelect();
  const assessmentOptions = assessments?.data ?? [];
  const { data: facilities, isLoading: isFacilityLoading } =
    useFacilityForSelect();
  const facilityOptions = facilities?.data ?? [];
  const createMutation = useCreateAdmission();
  const close = useDialogStore((s) => s.close);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { assessmentId: "", facilityId: "", admissionDate: "" },
  });

  const onSubmit = (values: z.infer<typeof formSchema>) => {
    createMutation.mutate(
      {
        assessmentId: Number(values.assessmentId),
        facilityId: Number(values.facilityId),
        admissionDate: values.admissionDate,
      },
      { onSuccess: close },
    );
  };

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <FieldGroup>
        <Controller
          name="assessmentId"
          control={form.control}
          render={({ field, fieldState }) => (
            <Field data-invalid={fieldState.invalid}>
              <FieldLabel htmlFor={field.name}>Assessment</FieldLabel>
              <Select
                name={field.name}
                value={field.value}
                onValueChange={field.onChange}
              >
                <SelectTrigger
                  id={field.name}
                  aria-invalid={fieldState.invalid}
                >
                  <SelectValue
                    placeholder={isLoading ? "Loading..." : "Select assessment"}
                  >
                    {
                      assessmentOptions.find(
                        (a) => String(a.id) === field.value,
                      )?.residentName
                    }
                  </SelectValue>
                </SelectTrigger>
                <SelectContent>
                  {assessmentOptions.map((a) => (
                    <SelectItem key={a.id} value={String(a.id)}>
                      {a.residentName} (ADL: {a.adlTotalScore})
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
            </Field>
          )}
        />
        <Controller
          name="facilityId"
          control={form.control}
          render={({ field, fieldState }) => (
            <Field data-invalid={fieldState.invalid}>
              <FieldLabel htmlFor={field.name}>Facility</FieldLabel>
              <Select
                name={field.name}
                value={field.value}
                onValueChange={field.onChange}
              >
                <SelectTrigger
                  id={field.name}
                  aria-invalid={fieldState.invalid}
                >
                  <SelectValue
                    placeholder={
                      isFacilityLoading ? "Loading..." : "Select facility"
                    }
                  >
                    {
                      facilityOptions.find((f) => String(f.id) === field.value)
                        ?.name
                    }
                  </SelectValue>
                </SelectTrigger>
                <SelectContent>
                  {facilityOptions.map((f) => (
                    <SelectItem key={f.id} value={String(f.id)}>
                      {f.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
            </Field>
          )}
        />
        <Controller
          name="admissionDate"
          control={form.control}
          render={({ field, fieldState }) => (
            <Field data-invalid={fieldState.invalid}>
              <FieldLabel htmlFor={field.name}>Admission Date</FieldLabel>
              <Input
                {...field}
                id={field.name}
                type="date"
                aria-invalid={fieldState.invalid}
              />
              {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
            </Field>
          )}
        />
      </FieldGroup>
      <Button type="submit" disabled={createMutation.isPending}>
        Create
      </Button>
    </form>
  );
};
