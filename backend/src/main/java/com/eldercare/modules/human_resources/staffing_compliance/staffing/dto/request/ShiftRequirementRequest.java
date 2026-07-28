package com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ShiftRequirementRequest {

    @NotNull
    @JsonProperty("shift_id")
    private Long shiftId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @JsonProperty("required_cna_hours")
    private BigDecimal requiredCnaHours;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @JsonProperty("required_nurse_hours")
    private BigDecimal requiredNurseHours;
}
