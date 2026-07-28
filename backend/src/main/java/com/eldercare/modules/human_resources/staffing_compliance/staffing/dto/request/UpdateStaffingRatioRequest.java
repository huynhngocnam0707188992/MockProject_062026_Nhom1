package com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateStaffingRatioRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    @JsonProperty("min_hrs_per_resident_day")
    private BigDecimal minHrsPerResidentDay;

    @NotNull
    @Min(0)
    @Max(100)
    @JsonProperty("warn_below_percentage")
    private Integer warnBelowPercentage;

    @Valid
    @NotEmpty
    @JsonProperty("shift_requirements")
    private List<ShiftRequirementRequest> shiftRequirements;
}
