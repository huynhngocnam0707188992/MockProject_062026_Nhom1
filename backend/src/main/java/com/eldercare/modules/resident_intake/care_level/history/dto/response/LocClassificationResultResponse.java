package com.eldercare.modules.resident_intake.care_level.history.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocClassificationResultResponse {

    private Long residentId;

    private String residentName;

    private Long assessmentId;

    private LocalDate assessmentDate;

    private Integer adlScore;

    private CareLevelInfo suggestedLevel;

    private List<AdlItemResponse> adlItems;

    private BigDecimal dailyRate;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareLevelInfo {

        private Long id;

        private String levelCode;

        private String levelName;
    }
}