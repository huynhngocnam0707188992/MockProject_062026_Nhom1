package com.eldercare.modules.facility.dto.response;

import com.eldercare.common.enums.BedStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BedResponse {
    private Long id;
    
    @JsonProperty("bed_number")
    private String bedNumber;
    
    private BedStatus status;
    
    @JsonProperty("room_id")
    private Long roomId;
}