import { useEffect, useMemo, useState } from "react";
import {
    AlertCircle,
    CheckCircle2,
    Loader2,
    ShieldAlert,
} from "lucide-react";
import {
    useStaffingRatio,
    useUpdateStaffingRatio,
} from "../hooks/use-staffing-ratio";
import type { ShiftRequirement } from "../types/staffing-ratio.type";

const formatTime = (value: string) => value.slice(0, 5);

const formatShiftName = (shift: ShiftRequirement) => {
    const name = shift.shiftName.charAt(0) + shift.shiftName.slice(1).toLowerCase();
    return `${name} (${formatTime(shift.startTime)}–${formatTime(shift.endTime)})`;
};

const StaffingRatioPage = () => {
    const { data, isLoading, isError } = useStaffingRatio();
    const updateMutation = useUpdateStaffingRatio();

    const [minHours, setMinHours] = useState("");
    const [warnBelowPercentage, setWarnBelowPercentage] = useState("");
    const [shiftRequirements, setShiftRequirements] = useState<ShiftRequirement[]>([]);

    const resetForm = () => {
        if (!data) return;

        setMinHours(String(data.minHrsPerResidentDay));
        setWarnBelowPercentage(String(data.warnBelowPercentage));
        setShiftRequirements(data.shiftRequirements.map((item) => ({ ...item })));
    };

    useEffect(() => {
        resetForm();
        // data is the only source used to reset the form after GET/PUT succeeds.
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [data]);

    const totals = useMemo(() => {
        const totalCnaHours = shiftRequirements.reduce(
            (sum, shift) => sum + shift.requiredCnaHours,
            0,
        );
        const totalNurseHours = shiftRequirements.reduce(
            (sum, shift) => sum + shift.requiredNurseHours,
            0,
        );

        return {
            totalCnaHours,
            totalNurseHours,
            sumOfShifts: totalCnaHours + totalNurseHours,
        };
    }, [shiftRequirements]);

    const isDirty = useMemo(() => {
        if (!data) return false;

        return (
            minHours !== String(data.minHrsPerResidentDay) ||
            warnBelowPercentage !== String(data.warnBelowPercentage) ||
            JSON.stringify(shiftRequirements) !==
            JSON.stringify(data.shiftRequirements)
        );
    }, [data, minHours, shiftRequirements, warnBelowPercentage]);

    const minHoursNumber = Number(minHours);
    const warningNumber = Number(warnBelowPercentage);
    const hasInvalidShiftHours = shiftRequirements.some(
        (shift) =>
            !Number.isFinite(shift.requiredCnaHours) ||
            !Number.isFinite(shift.requiredNurseHours) ||
            shift.requiredCnaHours < 0 ||
            shift.requiredNurseHours < 0,
    );
    const isFormValid =
        Number.isFinite(minHoursNumber) &&
        minHoursNumber > 0 &&
        Number.isInteger(warningNumber) &&
        warningNumber >= 0 &&
        warningNumber <= 100 &&
        shiftRequirements.length > 0 &&
        !hasInvalidShiftHours;

    const updateShiftHour = (
        shiftId: number,
        field: "requiredCnaHours" | "requiredNurseHours",
        rawValue: string,
    ) => {
        const value = rawValue === "" ? 0 : Number(rawValue);
        if (!Number.isFinite(value)) return;

        setShiftRequirements((current) =>
            current.map((shift) => {
                if (shift.shiftId !== shiftId) return shift;

                const updated = { ...shift, [field]: value };
                return {
                    ...updated,
                    subtotal:
                        updated.requiredCnaHours + updated.requiredNurseHours,
                };
            }),
        );
    };

    const handleSave = () => {
        if (!isFormValid) return;

        updateMutation.mutate({
            minHrsPerResidentDay: minHoursNumber,
            warnBelowPercentage: warningNumber,
            shiftRequirements: shiftRequirements.map((shift) => ({
                shiftId: shift.shiftId,
                requiredCnaHours: shift.requiredCnaHours,
                requiredNurseHours: shift.requiredNurseHours,
            })),
        });
    };

    if (isLoading) {
        return (
            <div className="flex items-center justify-center gap-2 p-10 text-sm text-gray-500">
                <Loader2 className="h-4 w-4 animate-spin" />
                Loading staffing ratio...
            </div>
        );
    }

    if (isError) {
        return (
            <div className="mx-auto mt-6 flex max-w-md items-start gap-3 rounded-lg border border-red-200 bg-red-50 p-4 text-sm text-red-800">
                <AlertCircle className="mt-0.5 h-5 w-5 shrink-0 text-red-600" />
                Failed to load staffing ratio. Please refresh the page or try again later.
            </div>
        );
    }

    return (
        <div className="flex min-h-full flex-col">
            <div className="mx-auto w-full max-w-6xl flex-1 space-y-6 p-4 sm:p-6">
                <div className="space-y-1">
                    <p className="text-sm text-gray-500">Admin &gt; Staffing</p>
                    <h1 className="text-2xl font-bold tracking-tight text-gray-900 sm:text-3xl">
                        Staffing Ratio Configuration
                    </h1>
                    <p className="max-w-2xl text-sm text-gray-500">
                        California SNF minimum: {minHours} direct care hours per resident per day (BR-01)
                    </p>
                </div>

                <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
                    <h2 className="mb-4 text-lg font-bold text-gray-900 sm:text-xl">
                        Minimum Requirement
                    </h2>

                    <div className="grid grid-cols-1 gap-5 md:grid-cols-3">
                        <div>
                            <label className="mb-2 block text-sm font-semibold text-gray-700">
                                Target State
                            </label>
                            <input
                                value="California"
                                readOnly
                                className="h-11 w-full rounded-md border border-gray-300 bg-gray-50 px-4 text-sm text-gray-600"
                            />
                        </div>

                        <div>
                            <label className="mb-2 block text-sm font-semibold text-gray-700">
                                Minimum Direct Care Hours
                            </label>
                            <div className="flex h-11 items-center rounded-md border border-gray-300 bg-white px-4 focus-within:border-blue-400 focus-within:ring-2 focus-within:ring-blue-100">
                                <input
                                    type="number"
                                    min="0.01"
                                    step="0.1"
                                    value={minHours}
                                    onChange={(event) => setMinHours(event.target.value)}
                                    className="w-full text-sm text-gray-700 outline-none"
                                />
                                <span className="shrink-0 text-xs text-gray-400">
                                    hrs/resident/day
                                </span>
                            </div>
                        </div>

                        <div>
                            <label className="mb-2 block text-sm font-semibold text-gray-700">
                                Warn Below Percentage
                            </label>
                            <div className="flex h-11 items-center rounded-md border border-gray-300 bg-white px-4 focus-within:border-blue-400 focus-within:ring-2 focus-within:ring-blue-100">
                                <input
                                    type="number"
                                    min="0"
                                    max="100"
                                    step="1"
                                    value={warnBelowPercentage}
                                    onChange={(event) =>
                                        setWarnBelowPercentage(event.target.value)
                                    }
                                    className="w-full text-sm text-gray-700 outline-none"
                                />
                                <span className="shrink-0 text-xs text-gray-400">%</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <h2 className="mb-4 text-lg font-bold text-gray-900 sm:text-xl">
                        Shift Breakdown
                    </h2>

                    <div className="overflow-hidden rounded-lg border border-gray-200 bg-white shadow-sm">
                        <div className="overflow-x-auto">
                            <table className="w-full text-left text-sm">
                                <thead className="bg-gray-50 text-xs uppercase tracking-wide text-gray-500">
                                    <tr>
                                        <th className="px-4 py-3 font-semibold">Shift</th>
                                        <th className="px-4 py-3 font-semibold">Required CNA hrs</th>
                                        <th className="px-4 py-3 font-semibold">Required Nurse hrs</th>
                                        <th className="px-4 py-3 font-semibold">Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-100">
                                    {shiftRequirements.map((shift) => (
                                        <tr key={shift.shiftId} className="hover:bg-gray-50">
                                            <td className="whitespace-nowrap px-4 py-3.5 text-gray-700">
                                                {formatShiftName(shift)}
                                            </td>
                                            <td className="px-4 py-3.5">
                                                <div className="flex w-32 items-center rounded-md border border-gray-300 bg-white px-3 focus-within:border-blue-400 focus-within:ring-2 focus-within:ring-blue-100">
                                                    <input
                                                        aria-label={`${shift.shiftName} required CNA hours`}
                                                        type="number"
                                                        min="0"
                                                        step="0.1"
                                                        value={shift.requiredCnaHours}
                                                        onChange={(event) =>
                                                            updateShiftHour(
                                                                shift.shiftId,
                                                                "requiredCnaHours",
                                                                event.target.value,
                                                            )
                                                        }
                                                        className="h-9 w-full outline-none"
                                                    />
                                                    <span className="text-xs text-gray-400">hrs</span>
                                                </div>
                                            </td>
                                            <td className="px-4 py-3.5">
                                                <div className="flex w-32 items-center rounded-md border border-gray-300 bg-white px-3 focus-within:border-blue-400 focus-within:ring-2 focus-within:ring-blue-100">
                                                    <input
                                                        aria-label={`${shift.shiftName} required nurse hours`}
                                                        type="number"
                                                        min="0"
                                                        step="0.1"
                                                        value={shift.requiredNurseHours}
                                                        onChange={(event) =>
                                                            updateShiftHour(
                                                                shift.shiftId,
                                                                "requiredNurseHours",
                                                                event.target.value,
                                                            )
                                                        }
                                                        className="h-9 w-full outline-none"
                                                    />
                                                    <span className="text-xs text-gray-400">hrs</span>
                                                </div>
                                            </td>
                                            <td className="px-4 py-3.5 font-semibold text-gray-900">
                                                {shift.subtotal.toFixed(2)} hrs
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                                <tfoot>
                                    <tr className="border-t border-gray-200 bg-gray-50">
                                        <td className="px-4 py-3 font-semibold text-gray-900">Sum of shifts</td>
                                        <td className="px-4 py-3 text-gray-600">
                                            CNA: {totals.totalCnaHours.toFixed(2)} hrs
                                        </td>
                                        <td className="px-4 py-3 text-gray-600">
                                            Nurse: {totals.totalNurseHours.toFixed(2)} hrs
                                        </td>
                                        <td className="px-4 py-3 font-semibold text-gray-900">
                                            {totals.sumOfShifts.toFixed(2)} hrs/resident/day
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>

                {!isFormValid && (
                    <div className="flex items-start gap-3 rounded-lg border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                        <AlertCircle className="mt-0.5 h-5 w-5 shrink-0" />
                        Enter non-negative CNA and Nurse hours, a minimum direct care value greater than zero, and a warning percentage from 0 to 100.
                    </div>
                )}

                <div className="rounded-xl border border-emerald-200 bg-emerald-50 p-5 text-sm text-emerald-800">
                    <h3 className="mb-3 flex items-center gap-2 font-bold">
                        <ShieldAlert className="h-4 w-4 text-emerald-600" />
                        Current Compliance (simulated)
                    </h3>
                    <p className="text-emerald-700">
                        Facility ID: {data?.facilityId} · Warning threshold: {warnBelowPercentage}%
                    </p>
                    <p className="mt-1 flex items-center gap-1.5 font-semibold">
                        <CheckCircle2 className="h-4 w-4 text-emerald-600" />
                        Minimum requirement: {minHours} hrs/resident/day → Configured
                    </p>
                </div>

                {updateMutation.isSuccess && (
                    <div className="flex items-start gap-3 rounded-lg border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-800">
                        <CheckCircle2 className="mt-0.5 h-5 w-5 shrink-0 text-emerald-600" />
                        Staffing ratio settings saved.
                    </div>
                )}

                {updateMutation.isError && (
                    <div className="flex items-start gap-3 rounded-lg border border-red-200 bg-red-50 p-4 text-sm text-red-800">
                        <AlertCircle className="mt-0.5 h-5 w-5 shrink-0 text-red-600" />
                        Failed to save staffing ratio settings. Please try again.
                    </div>
                )}
            </div>

            <div className="sticky bottom-0 flex flex-col items-center gap-3 border-t border-gray-200 bg-white p-4">
                {isDirty && (
                    <span className="text-sm text-amber-600">You have unsaved changes.</span>
                )}

                <div className="flex w-full flex-col gap-3 sm:w-auto sm:flex-row">
                    <button
                        type="button"
                        onClick={resetForm}
                        disabled={!isDirty || updateMutation.isPending}
                        className="h-11 rounded-md border border-gray-300 bg-white px-6 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        Cancel
                    </button>

                    <button
                        type="button"
                        onClick={handleSave}
                        disabled={updateMutation.isPending || !isDirty || !isFormValid}
                        className="flex h-11 items-center justify-center gap-2 rounded-md bg-blue-600 px-6 text-sm font-bold text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        {updateMutation.isPending && <Loader2 className="h-4 w-4 animate-spin" />}
                        {updateMutation.isPending ? "Saving..." : "Save Changes"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default StaffingRatioPage;
