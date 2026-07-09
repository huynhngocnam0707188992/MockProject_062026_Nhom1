package com.eldercare.modules.facility.mapper;

import com.eldercare.modules.facility.dto.request.AddressDto;
import com.eldercare.modules.facility.dto.request.FacilityCreateRequest;
import com.eldercare.modules.facility.dto.request.FacilityUpdateRequest;
import com.eldercare.modules.facility.dto.response.FacilityResponse;
import com.eldercare.modules.facility.entity.Address;
import com.eldercare.modules.facility.entity.Facility;
import org.springframework.stereotype.Component;

@Component
public class FacilityMapper {

    public Facility toEntity(FacilityCreateRequest request) {
        if (request == null) {
            return null;
        }

        Facility facility = new Facility();
        facility.setFacilityCode(request.getFacilityCode());
        facility.setName(request.getName());
        facility.setLicenseNumber(request.getLicenseNumber());
        facility.setTargetState(request.getTargetState());
        facility.setPhoneNumber(request.getPhoneNumber());

        if (request.getAddress() != null) {
            Address address = new Address();
            address.setStreetLine1(request.getAddress().getStreetLine1());
            address.setStreetLine2(request.getAddress().getStreetLine2());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setZipCode(request.getAddress().getZipCode());
            address.setAddressType(request.getAddress().getAddressType());
            facility.setAddress(address);
        }

        return facility;
    }

    public FacilityResponse toResponse(Facility facility) {
        if (facility == null) {
            return null;
        }

        FacilityResponse response = new FacilityResponse();
        response.setId(facility.getId());
        response.setFacilityCode(facility.getFacilityCode());
        response.setName(facility.getName());
        response.setLicenseNumber(facility.getLicenseNumber());
        response.setTargetState(facility.getTargetState());
        response.setPhoneNumber(facility.getPhoneNumber());
        response.setUpdatedAt(facility.getUpdatedAt());
        
        if (facility.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreetLine1(facility.getAddress().getStreetLine1());
            addressDto.setStreetLine2(facility.getAddress().getStreetLine2());
            addressDto.setCity(facility.getAddress().getCity());
            addressDto.setState(facility.getAddress().getState());
            addressDto.setZipCode(facility.getAddress().getZipCode());
            addressDto.setAddressType(facility.getAddress().getAddressType());
            response.setAddress(addressDto);
        }

        return response;
    }

    public void updateEntity(Facility facility, FacilityUpdateRequest request) {
        if (request.getName() != null) facility.setName(request.getName());
        if (request.getFacilityCode() != null) facility.setFacilityCode(request.getFacilityCode());
        if (request.getLicenseNumber() != null) facility.setLicenseNumber(request.getLicenseNumber());
        if (request.getTargetState() != null) facility.setTargetState(request.getTargetState());
        if (request.getPhoneNumber() != null) facility.setPhoneNumber(request.getPhoneNumber());

        if (request.getAddress() != null) {
            Address address = facility.getAddress();
            if (address == null) {
                address = new Address();
                facility.setAddress(address);
            }
            address.setStreetLine1(request.getAddress().getStreetLine1());
            address.setStreetLine2(request.getAddress().getStreetLine2());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setZipCode(request.getAddress().getZipCode());
            address.setAddressType(request.getAddress().getAddressType());
        }
    }
}