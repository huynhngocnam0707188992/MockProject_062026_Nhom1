package com.eldercare.modules.carelevel.dto.response;

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