package com.eldercare.modules.carelevel.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCareLevelRequest {

    private Boolean isDeleted;

}