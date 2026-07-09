package com.eldercare.modules.facility.dto.request;

import com.eldercare.common.enums.BedStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BedRequest {
    @JsonProperty("bed_number")
    private String bedNumber;
    
    private BedStatus status;
}