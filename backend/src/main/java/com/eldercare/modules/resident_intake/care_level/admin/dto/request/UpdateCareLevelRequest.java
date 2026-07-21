package com.eldercare.modules.resident_intake.care_level.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCareLevelRequest {

    @NotNull(message = "isDeleted is required")
    @JsonProperty("is_deleted")
    private Boolean isDeleted;

}