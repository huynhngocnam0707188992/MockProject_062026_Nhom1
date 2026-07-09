package com.eldercare.modules.facility.dto.response;

import com.eldercare.common.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    
    @JsonProperty("room_number")
    private String roomNumber;
    
    @JsonProperty("room_type")
    private RoomType roomType;
    
    @JsonProperty("facility_id")
    private Long facilityId;
    
    @JsonProperty("is_deleted")
    private boolean isDeleted;
}