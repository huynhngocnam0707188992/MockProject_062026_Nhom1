import { useEffect } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useFieldArray, useForm } from "react-hook-form";
import z from "zod";

import { Button } from "@/components/ui/button";
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
  FieldLegend,
  FieldSet,
} from "@/components/ui/field";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";

import { useCreateAssessment } from "../hooks/use-create-assessment";
import { useAssessmentMetrics } from "../hooks/use-assessment-metrics";
import { useCompletedScreenings } from "../../pre-admission/hooks/use-completed-screenings";
import { useDialogStore } from "@/store/use-dialog-store";

const formSchema = z.object({
  preAdmissionScreeningId: z.string().min(1, "Please select a screening."),
  details: z
    .array(
      z.object({
        metricId: z.number(),
        score: z.number().min(0, "Score must be >= 0."),
        notes: z.string().optional(),
      }),
    )
    .min(1, "No metrics available."),
});

export const CreateAssessmentForm = () => {
  const { data: screenings, isLoading: loadingScreenings } =
    useCompletedScreenings();
  const { data: metrics, isLoading: loadingMetrics } = useAssessmentMetrics();
  const screeningOptions = screenings?.data ?? [];
  const metricOptions = metrics?.data ?? [];

  const createMutation = useCreateAssessment();
  const close = useDialogStore((s) => s.close);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { preAdmissionScreeningId: "", details: [] },
  });

  const { fields, replace } = useFieldArray({
    control: form.control,
    name: "details",
  });

  useEffect(() => {
    if (metricOptions.length > 0 && fields.length === 0) {
      replace(
        metricOptions.map((m) => ({ metricId: m.id, score: 0, notes: "" })),
      );
    }
  }, [metricOptions, fields.length, replace]);

  const onSubmit = (values: z.infer<typeof formSchema>) => {
    createMutation.mutate(
      {
        preAdmissionScreeningId: Number(values.preAdmissionScreeningId),
        details: values.details,
      },
      { onSuccess: close },
    );
  };

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <FieldGroup>
        <Controller
          name="preAdmissionScreeningId"
          control={form.control}
          render={({ field, fieldState }) => (
            <Field data-invalid={fieldState.invalid}>
              <FieldLabel htmlFor={field.name}>
                Pre-Admission Screening
              </FieldLabel>
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
                      loadingScreenings ? "Loading..." : "Select screening"
                    }
                  >
                    {
                      screeningOptions.find((s) => String(s.id) === field.value)
                        ?.residentName
                    }
                  </SelectValue>
                </SelectTrigger>
                <SelectContent>
                  {screeningOptions.map((s) => (
                    <SelectItem key={s.id} value={String(s.id)}>
                      #{s.id} - {s.residentName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
            </Field>
          )}
        />

        <FieldSet>
          <FieldLegend variant="label">Metric Scores</FieldLegend>
          {loadingMetrics && (
            <p className="text-sm text-muted-foreground">Loading metrics...</p>
          )}
          {!loadingMetrics && fields.length === 0 && (
            <p className="text-sm text-muted-foreground">
              No metrics available.
            </p>
          )}
          <FieldGroup className="gap-3">
            {fields.map((item, index) => {
              const metric = metricOptions.find((m) => m.id === item.metricId);
              return (
                <Controller
                  key={item.id}
                  name={`details.${index}.score`}
                  control={form.control}
                  render={({ field, fieldState }) => (
                    <Field
                      orientation="horizontal"
                      data-invalid={fieldState.invalid}
                    >
                      <FieldLabel htmlFor={field.name} className="flex-1">
                        {metric?.metricName ?? `Metric #${item.metricId}`}
                        <span className="text-muted-foreground ml-1 text-xs">
                          ({metric?.category})
                        </span>
                      </FieldLabel>
                      <Input
                        {...field}
                        id={field.name}
                        type="number"
                        className="w-24"
                        aria-invalid={fieldState.invalid}
                        onChange={(e) => field.onChange(Number(e.target.value))}
                      />
                      {fieldState.invalid && (
                        <FieldError errors={[fieldState.error]} />
                      )}
                    </Field>
                  )}
                />
              );
            })}
          </FieldGroup>
        </FieldSet>
      </FieldGroup>
      <Button type="submit" disabled={createMutation.isPending}>
        Create
      </Button>
    </form>
  );
};
