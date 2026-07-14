package com.eldercare.modules.resident_intake.care_level.history.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdlItemResponse {

    private String activity;

    private Integer score;

    private Integer maxScore;

    private String source;
}