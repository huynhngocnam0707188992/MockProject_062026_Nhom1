package com.eldercare.modules.resident_intake.care_level.admin.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareLevelResponse {

    private Long id;

    private String levelCode;

    private String levelName;

    private Boolean isDeleted;

}