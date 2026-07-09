package com.eldercare.modules.facility.dto.response;

import com.eldercare.modules.facility.dto.request.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class FacilityResponse {
    private Long id;
    
    @JsonProperty("facility_code")
    private String facilityCode;
    
    private String name;
    
    @JsonProperty("license_number")
    private String licenseNumber;
    
    @JsonProperty("target_state")
    private String targetState;
    
    private AddressDto address;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
    
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}