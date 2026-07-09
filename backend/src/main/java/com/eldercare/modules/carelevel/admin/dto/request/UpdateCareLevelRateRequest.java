package com.eldercare.modules.carelevel.admin.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCareLevelRateRequest {

    private Long careLevelId;

    private Long facilityId;

    private BigDecimal dailyRate;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}