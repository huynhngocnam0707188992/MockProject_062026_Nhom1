package com.eldercare.modules.carelevel.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareLevelRateResponse {

    private Long id;

    private Long careLevelId;

    private Long facilityId;

    private BigDecimal dailyRate;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}