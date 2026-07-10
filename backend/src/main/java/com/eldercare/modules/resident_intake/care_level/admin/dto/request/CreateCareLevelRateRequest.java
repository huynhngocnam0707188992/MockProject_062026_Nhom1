package com.eldercare.modules.resident_intake.care_level.admin.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCareLevelRateRequest {

    private Long careLevelId;

    private Long facilityId;

    private BigDecimal dailyRate;

    private LocalDate effectiveFrom;

}