package com.eldercare.modules.resident_intake.resident.service;

import com.eldercare.modules.admin.facility_setup.facility.facility_profile.repository.AddressRepository;
import com.eldercare.modules.admin.facility_setup.facility.facility_layout.repository.BedRepository;
import com.eldercare.modules.admin.facility_setup.facility.facility_layout.repository.RoomRepository;
import com.eldercare.modules.resident_intake.care_level.admin.repository.CareLevelRepository;

import com.eldercare.modules.resident_intake.care_level.ResidentCareLevelHistoryEntity;
import com.eldercare.modules.finance_billing.insurance_coverage.ResidentInsurancePolicyEntity;
import com.eldercare.modules.resident_intake.resident_profile.ResidentSensitiveInfoEntity;
import com.eldercare.modules.finance_billing.insurance_coverage.InsuranceProviderEntity;
import com.eldercare.modules.resident_intake.family_contacts.ResidentContactEntity;
import com.eldercare.modules.clinical.clinical_record.ClinicalRecordEntity;
import com.eldercare.modules.resident_intake.admission_ledger.AdmissionEntity;
import com.eldercare.modules.resident_intake.admission_ledger.repository.AdmissionRepository;
import com.eldercare.modules.resident_intake.care_level.CareLevelEntity;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.AddressEntity;
import com.eldercare.modules.resident_intake.family_contacts.ContactEntity;
import com.eldercare.modules.admin.facility_setup.facility.facility_layout.entity.RoomEntity;
import com.eldercare.modules.admin.facility_setup.facility.facility_layout.entity.BedEntity;
import com.eldercare.common.enums.AddressType;
import com.eldercare.common.enums.RoomType;
import com.eldercare.common.enums.BedStatus;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.entity.FacilityEntity;
import com.eldercare.modules.admin.facility_setup.facility.facility_profile.repository.FacilityRepository;

import com.eldercare.modules.resident_intake.resident.dto.request.*;
import com.eldercare.modules.resident_intake.resident.dto.response.*;

import com.eldercare.modules.resident_intake.resident.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import com.eldercare.modules.resident_intake.resident.mapper.ResidentMapper;

@Service
@Transactional
public class ResidentServiceImpl implements ResidentService {

    @Autowired
    private ResidentMapper residentMapper;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ResidentContactRepository residentContactRepository;

    @Autowired
    private ResidentInsurancePolicyRepository residentInsurancePolicyRepository;

    @Autowired
    private InsuranceProviderRepository insuranceProviderRepository;

    @Autowired
    private ResidentSensitiveInfoRepository residentSensitiveInfoRepository;

    @Autowired
    private ClinicalRecordRepository clinicalRecordRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private ResidentCareLevelHistoryRepository residentCareLevelHistoryRepository;

    @Autowired
    private CareLevelRepository careLevelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResidentListResponseDto> getResidents(String search, String status, String referral) {
        List<ResidentEntity> residents = residentRepository.findByIsDeletedFalse();

        // Filter by status
        if (status != null && !status.equalsIgnoreCase("All")) {
            residents = residents.stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        List<ResidentListResponseDto> dtos = residents.stream()
                .map(r -> {
                    List<ResidentInsurancePolicyEntity> policies = residentInsurancePolicyRepository
                            .findByResidentIdAndIsDeletedFalse(r.getId());
                    return residentMapper.toListDto(r, policies);
                })
                .collect(Collectors.toList());

        // Filter by search
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            dtos = dtos.stream()
                    .filter(d -> d.getName().toLowerCase().contains(searchLower) ||
                            (d.getRoom() != null && d.getRoom().toLowerCase().contains(searchLower)) ||
                            String.valueOf(d.getId()).contains(searchLower))
                    .collect(Collectors.toList());
        }

        // Filter by referral
        if (referral != null && !referral.equalsIgnoreCase("All")) {
            dtos = dtos.stream()
                    .filter(d -> d.getReferralSource().equalsIgnoreCase(referral))
                    .collect(Collectors.toList());
        }

        return dtos;
    }

    private ResidentListResponseDto mapToListDto(ResidentEntity resident) {
        ResidentListResponseDto dto = new ResidentListResponseDto();
        dto.setId(resident.getId());
        dto.setName(resident.getFirstName() + " " + resident.getLastName());

        // RoomEntity mapping
        if (resident.getBed() != null) {
            String roomNum = resident.getBed().getRoom().getRoomNumber();
            String bedNum = resident.getBed().getBedNumber();
            dto.setRoom(roomNum + "-" + bedNum);
        } else {
            dto.setRoom("—");
        }

        // Status casing format
        String statusStr = resident.getStatus();
        if (statusStr != null && !statusStr.isEmpty()) {
            dto.setStatus(Character.toUpperCase(statusStr.charAt(0)) + statusStr.substring(1).toLowerCase());
        } else {
            dto.setStatus("Pending");
        }

        dto.setDob(resident.getDateOfBirth());

        // Age calculation
        if (resident.getDateOfBirth() != null) {
            dto.setAge(Period.between(resident.getDateOfBirth(), LocalDate.now()).getYears());
        } else {
            dto.setAge(75);
        }

        // Payer source mapping
        List<ResidentInsurancePolicyEntity> policies = residentInsurancePolicyRepository
                .findByResidentIdAndIsDeletedFalse(resident.getId());
        Optional<ResidentInsurancePolicyEntity> primaryPolicy = policies.stream()
                .filter(ResidentInsurancePolicyEntity::isPrimary).findFirst();
        if (primaryPolicy.isPresent()) {
            dto.setPayerSource(primaryPolicy.get().getInsuranceProvider().getProviderName());
        } else {
            dto.setPayerSource("Private Pay");
        }

        // Referral source (Mock mapping because database has no referral table)
        dto.setReferralSource(getMockReferralSource(resident.getId()));

        return dto;
    }

    private String getMockReferralSource(Long id) {
        if (id == null)
            return "Private";
        int index = (int) (id % 5);
        switch (index) {
            case 1:
                return "Sunrise Regional Hosp.";
            case 2:
                return "Private";
            case 3:
                return "Family";
            case 4:
                return "Self";
            case 0:
                return "Valley General Hosp.";
            default:
                return "Private";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentDetailResponseDto getResidentDetail(Long id) {
        ResidentEntity resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        List<ResidentCareLevelHistoryEntity> careHistory = residentCareLevelHistoryRepository
                .findByResidentId(resident.getId());
        List<ResidentInsurancePolicyEntity> policies = residentInsurancePolicyRepository
                .findByResidentIdAndIsDeletedFalse(resident.getId());
        Optional<ResidentSensitiveInfoEntity> sensitive = residentSensitiveInfoRepository
                .findByResidentId(resident.getId());
        List<AdmissionEntity> admissions = admissionRepository.findByResidentId(resident.getId());
        List<ResidentContactEntity> residentContacts = residentContactRepository.findByResidentId(resident.getId());
        List<ClinicalRecordEntity> diagnoses = clinicalRecordRepository
                .findByResidentIdAndRecordTypeAndIsDeletedFalse(resident.getId(), "DIAGNOSIS");
        List<ClinicalRecordEntity> allergies = clinicalRecordRepository
                .findByResidentIdAndRecordTypeAndIsDeletedFalse(resident.getId(), "ALLERGY");

        return residentMapper.toDetailResponseDto(resident, careHistory, policies, sensitive, admissions,
                residentContacts, diagnoses, allergies);
    }

    @Override
    public void saveResident(Long id, ResidentSaveRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        updateResidentFields(resident, dto);
        residentRepository.save(resident);
    }

    @Override
    public void createResident(ResidentSaveRequestDto dto) {
        ResidentEntity resident = new ResidentEntity();
        updateResidentFields(resident, dto);
        resident = residentRepository.save(resident);

        // Save Sensitive info (SSN)
        ResidentSensitiveInfoEntity sensitive = new ResidentSensitiveInfoEntity();
        sensitive.setResident(resident);
        sensitive.setSsnEncrypted(dto.getSsn() != null ? dto.getSsn() : "XXX-XX-9999");
        sensitive.setMedicalRecordNumberEncrypted("MRN-" + resident.getId());
        residentSensitiveInfoRepository.save(sensitive);

        // Save AdmissionEntity record
        AdmissionEntity admission = new AdmissionEntity();
        admission.setResident(resident);
        admission.setAdmissionDate(LocalDate.now());
        admissionRepository.save(admission);

        // Save Care Level history
        CareLevelEntity careLevel = careLevelRepository.findByLevelNameAndIsDeletedFalse("Level 3")
                .orElseGet(() -> {
                    CareLevelEntity cl = new CareLevelEntity();
                    cl.setLevelCode("L3");
                    cl.setLevelName("Level 3");
                    return careLevelRepository.save(cl);
                });

        ResidentCareLevelHistoryEntity history = new ResidentCareLevelHistoryEntity();
        history.setResident(resident);
        history.setCareLevel(careLevel);
        history.setStartDate(LocalDate.now());
        residentCareLevelHistoryRepository.save(history);
    }

    private void updateResidentFields(ResidentEntity resident, ResidentSaveRequestDto dto) {
        resident.setFirstName(dto.getFirstName());
        resident.setLastName(dto.getLastName());
        resident.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PENDING");
        resident.setDateOfBirth(dto.getDob());
        resident.setGender(dto.getGender());
        resident.setMaritalStatus(dto.getMaritalStatus());
        resident.setUpdatedAt(OffsetDateTime.now());

        // AddressEntity mapping
        if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
            AddressEntity address = resident.getAddress();
            if (address == null) {
                address = new AddressEntity();
                address.setAddressType(AddressType.HOME);
            }
            address.setStreetLine1(dto.getAddress());
            address.setCity("Riverside");
            address.setState("CA");
            address.setZipCode("92501");
            address = addressRepository.save(address);
            resident.setAddress(address);
        }

        // RoomEntity and BedEntity mapping (e.g. "106-A" or "106" -> room 106, bed A)
        String roomBed = dto.getReferringFacility(); // UI puts room in referringFacility or maps room directly
        if (roomBed == null || roomBed.isEmpty()) {
            roomBed = "101-A";
        }

        String[] parts = roomBed.split("-");
        String roomNumber = parts[0];
        String bedLetter = parts.length > 1 ? parts[1] : "A";

        RoomEntity room = roomRepository.findByRoomNumberAndIsDeletedFalse(roomNumber)
                .orElseGet(() -> {
                    RoomEntity r = new RoomEntity();
                    r.setRoomNumber(roomNumber);
                    r.setRoomType(RoomType.SEMI_PRIVATE);
                    FacilityEntity facility = facilityRepository.findById(1L)
                            .orElseThrow(() -> new RuntimeException("Default Facility not found"));
                    r.setFacility(facility);
                    return roomRepository.save(r);
                });

        BedEntity bed = bedRepository.findByBedNumberAndRoomId(bedLetter, room.getId())
                .orElseGet(() -> {
                    BedEntity b = new BedEntity();
                    b.setBedNumber(bedLetter);
                    b.setStatus(BedStatus.OCCUPIED);
                    b.setRoom(room);
                    return bedRepository.save(b);
                });

        resident.setBed(bed);

        // Insurance mapping
        if (dto.getPayerSource() != null) {
            String providerName = dto.getPayerSource();
            InsuranceProviderEntity provider = insuranceProviderRepository.findByProviderName(providerName)
                    .orElseGet(() -> {
                        InsuranceProviderEntity p = new InsuranceProviderEntity();
                        p.setProviderName(providerName);
                        p.setProviderType(dto.getPayerType() != null ? dto.getPayerType().toUpperCase() : "PRIVATE");
                        return insuranceProviderRepository.save(p);
                    });

            List<ResidentInsurancePolicyEntity> policies = resident.getId() != null
                    ? residentInsurancePolicyRepository.findByResidentIdAndIsDeletedFalse(resident.getId())
                    : Collections.emptyList();

            ResidentInsurancePolicyEntity policy = policies.stream().filter(ResidentInsurancePolicyEntity::isPrimary)
                    .findFirst().orElse(null);
            if (policy == null) {
                policy = new ResidentInsurancePolicyEntity();
                policy.setResident(resident);
                policy.setPrimary(true);
            }
            policy.setInsuranceProvider(provider);
            policy.setPolicyNumberEncrypted(dto.getMedicareNum() != null ? dto.getMedicareNum() : "ENC-MCR-000000");
            policy.setEffectiveFrom(LocalDate.now());
            residentInsurancePolicyRepository.save(policy);
        }

        // Emergency ContactEntity
        if (dto.getEmergencyContact() != null) {
            String ecName = dto.getEmergencyContact();
            String[] nameParts = ecName.split(" ");
            String ecFirst = nameParts[0];
            String ecLast = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "ContactEntity";

            ContactEntity contact = new ContactEntity();
            contact.setFirstName(ecFirst);
            contact.setLastName(ecLast);
            contact.setPhonePrimary(dto.getEmergencyPhone() != null ? dto.getEmergencyPhone() : "555-000-0000");
            contact = contactRepository.save(contact);

            ResidentContactEntity rc = new ResidentContactEntity();
            rc.setResident(resident);
            rc.setContact(contact);
            rc.setRelationshipType(
                    dto.getPoaRelationship() != null ? dto.getPoaRelationship().toUpperCase() : "DAUGHTER");
            rc.setEmergencyContact(true);
            rc.setPrimary(true);
            residentContactRepository.save(rc);
        }
    }

    @Override
    public void deleteResident(Long id) {
        ResidentEntity resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));
        resident.setDeleted(true);
        resident.setStatus("DISCHARGED");
        resident.setDeletedAt(OffsetDateTime.now());
        residentRepository.save(resident);
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentListResponseContainerDto getResidentsV1(String status, Long bedId, String search, Integer page,
            Integer pageSize) {
        List<ResidentEntity> residents = residentRepository.findByIsDeletedFalse();

        // 1. Filter by status
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("All")) {
            residents = residents.stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase(status.trim()))
                    .collect(Collectors.toList());
        }

        // 2. Filter by bedId
        if (bedId != null) {
            residents = residents.stream()
                    .filter(r -> r.getBed() != null && r.getBed().getId().equals(bedId))
                    .collect(Collectors.toList());
        }

        // 3. Filter by search (name or id)
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            residents = residents.stream()
                    .filter(r -> r.getFirstName().toLowerCase().contains(searchLower) ||
                            r.getLastName().toLowerCase().contains(searchLower) ||
                            String.valueOf(r.getId()).contains(searchLower))
                    .collect(Collectors.toList());
        }

        long total = residents.size();

        // 4. Pagination
        int pageVal = (page != null && page > 0) ? page : 1;
        int pageSizeVal = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int offset = (pageVal - 1) * pageSizeVal;

        List<ResidentEntity> paginatedResidents;
        if (offset >= residents.size()) {
            paginatedResidents = Collections.emptyList();
        } else {
            paginatedResidents = residents.stream()
                    .skip(offset)
                    .limit(pageSizeVal)
                    .collect(Collectors.toList());
        }

        // 5. Map to DTO
        List<ResidentListResponseItemDto> listItems = paginatedResidents.stream()
                .map(r -> ResidentListResponseItemDto.builder()
                        .id(r.getId())
                        .firstName(r.getFirstName())
                        .lastName(r.getLastName())
                        .currentCareLevel(getActiveCareLevelCode(r.getId()))
                        .build())
                .collect(Collectors.toList());

        return ResidentListResponseContainerDto.builder()
                .residents(listItems)
                .meta(PaginationMetaDto.builder()
                        .total(total)
                        .page(pageVal)
                        .pageSize(pageSizeVal)
                        .build())
                .build();
    }

    private String getActiveCareLevelCode(Long residentId) {
        List<ResidentCareLevelHistoryEntity> histories = residentCareLevelHistoryRepository
                .findByResidentId(residentId);
        LocalDate today = LocalDate.now();
        Optional<ResidentCareLevelHistoryEntity> activeHistory = histories.stream()
                .filter(h -> !h.getStartDate().isAfter(today)
                        && (h.getEndDate() == null || !h.getEndDate().isBefore(today)))
                .findFirst();
        if (activeHistory.isPresent()) {
            return activeHistory.get().getCareLevel().getLevelCode();
        }
        // Fallback: most recent by startDate DESC
        return histories.stream()
                .sorted(Comparator.comparing(ResidentCareLevelHistoryEntity::getStartDate).reversed())
                .map(h -> h.getCareLevel().getLevelCode())
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentResponseDto getResidentByIdV1(Long id) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto createResidentV1(ResidentCreateRequestDto dto) {
        ResidentEntity resident = new ResidentEntity();
        resident.setFirstName(dto.getFirstName());
        resident.setMiddleName(dto.getMiddleName());
        resident.setLastName(dto.getLastName());
        resident.setDateOfBirth(dto.getDateOfBirth());
        resident.setGender(dto.getGender());
        resident.setMaritalStatus(dto.getMaritalStatus());
        resident.setReligionPreference(dto.getReligionPreference());
        resident.setStatus("PENDING");
        resident.setChartLocked(false);
        resident.setDeleted(false);
        resident.setCreatedAt(OffsetDateTime.now());
        resident.setUpdatedAt(OffsetDateTime.now());

        if (dto.getAddressId() != null) {
            addressRepository.findById(dto.getAddressId()).ifPresent(resident::setAddress);
        }

        resident = residentRepository.save(resident);
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto updateResidentV1(Long id, ResidentUpdateRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        if (resident.isChartLocked()) {
            throw new IllegalStateException(
                    "All clinical modifications are blocked because this resident's chart is locked.");
        }

        if (dto.getFirstName() != null)
            resident.setFirstName(dto.getFirstName());
        if (dto.getMiddleName() != null)
            resident.setMiddleName(dto.getMiddleName());
        if (dto.getLastName() != null)
            resident.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null)
            resident.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null)
            resident.setGender(dto.getGender());
        if (dto.getMaritalStatus() != null)
            resident.setMaritalStatus(dto.getMaritalStatus());
        if (dto.getReligionPreference() != null)
            resident.setReligionPreference(dto.getReligionPreference());
        if (dto.getAddressId() != null) {
            addressRepository.findById(dto.getAddressId()).ifPresent(resident::setAddress);
        }
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto updateResidentStatusV1(Long id, ResidentStatusUpdateRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        String newStatus = dto.getStatus().toUpperCase();
        resident.setStatus(newStatus);

        if (newStatus.equals("DISCHARGED") || newStatus.equals("DECEASED")) {
            resident.setBed(null);
        }
        if (newStatus.equals("DECEASED")) {
            resident.setChartLocked(true);
        }
        resident.setUpdatedAt(OffsetDateTime.now());

        // Simple audit logging representation
        if (dto.getReason() != null) {
            System.out.println("Audit Log - ResidentEntity ID: " + id + " Status change reason: " + dto.getReason());
        }

        resident = residentRepository.save(resident);
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto assignResidentBedV1(Long id, ResidentAssignBedRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        if (resident.isChartLocked()) {
            throw new IllegalStateException(
                    "All clinical modifications are blocked because this resident's chart is locked.");
        }

        if (dto.getBedId() == null) {
            resident.setBed(null);
        } else {
            BedEntity bed = bedRepository.findById(dto.getBedId())
                    .orElseThrow(() -> new IllegalArgumentException("BedEntity not found with id: " + dto.getBedId()));

            // Check if bed is already occupied by another active/non-discharged resident
            List<ResidentEntity> occupants = residentRepository.findByIsDeletedFalse();
            boolean occupied = occupants.stream()
                    .filter(r -> !r.getId().equals(id))
                    .filter(r -> r.getBed() != null && r.getBed().getId().equals(dto.getBedId()))
                    .anyMatch(r -> !r.getStatus().equalsIgnoreCase("DISCHARGED")
                            && !r.getStatus().equalsIgnoreCase("DECEASED"));

            if (occupied) {
                throw new IllegalStateException("BedEntity is already occupied by another active resident.");
            }
            resident.setBed(bed);
        }
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto lockResidentChartV1(Long id, ResidentChartLockRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("reason is required for audit logging");
        }

        System.out.println("Audit Log - ResidentEntity ID: " + id + " locked chart. Reason: " + dto.getReason());
        resident.setChartLocked(true);
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return residentMapper.toResponseDto(resident);
    }

    @Override
    public ResidentResponseDto unlockResidentChartV1(Long id, ResidentChartLockRequestDto dto) {
        ResidentEntity resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("ResidentEntity not found with id: " + id));

        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("reason is required for audit logging");
        }

        System.out.println("Audit Log - ResidentEntity ID: " + id + " unlocked chart. Reason: " + dto.getReason());
        resident.setChartLocked(false);
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    @Override
    public List<ResidentPendingDTO> getPendingResidents() {
        return residentRepository.findByStatus("PENDING").stream().map(r -> {
            ResidentPendingDTO dto = new ResidentPendingDTO();
            dto.setId(r.getId());
            dto.setFullName(r.getFirstName() + " " + r.getLastName());
            dto.setStatus(r.getStatus());
            return dto;
        }).toList();
    }

    private ResidentResponseDto mapToResponseDto(ResidentEntity resident) {
        return residentMapper.toResponseDto(resident);
    }
}
