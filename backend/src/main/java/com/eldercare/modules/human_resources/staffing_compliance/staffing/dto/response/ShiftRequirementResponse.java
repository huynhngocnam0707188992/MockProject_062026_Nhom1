package com.eldercare.modules.human_resources.staffing_compliance.staffing.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRequirementResponse {

    private Long id;

    @JsonProperty("shift_id")
    private Long shiftId;

    @JsonProperty("shift_name")
    private String shiftName;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("required_cna_hours")
    private BigDecimal requiredCnaHours;

    @JsonProperty("required_nurse_hours")
    private BigDecimal requiredNurseHours;

    private BigDecimal subtotal;
}
