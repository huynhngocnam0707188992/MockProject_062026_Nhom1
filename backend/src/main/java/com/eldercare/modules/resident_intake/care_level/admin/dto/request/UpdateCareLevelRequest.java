package com.eldercare.modules.resident_intake.care_level.admin.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCareLevelRequest {

    private Boolean isDeleted;

}