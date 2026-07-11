package com.eldercare.modules.admin.facility_setup.care_level.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCareLevelRequest {

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

}

