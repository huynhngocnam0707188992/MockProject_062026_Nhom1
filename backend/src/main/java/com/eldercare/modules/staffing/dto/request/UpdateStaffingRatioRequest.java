package com.eldercare.modules.staffing.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateStaffingRatioRequest {
    
    @JsonProperty("min_hrs_per_resident_day")
    private BigDecimal minHrsPerResidentDay;

    @JsonProperty("warn_below_percentage")
    private Integer warnBelowPercentage;
}