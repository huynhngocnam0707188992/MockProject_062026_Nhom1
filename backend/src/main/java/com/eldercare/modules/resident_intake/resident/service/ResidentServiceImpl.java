package com.eldercare.modules.resident_intake.resident.service;

import com.eldercare.modules.resident_intake.resident.dto.request.*;
import com.eldercare.modules.resident_intake.resident.dto.response.*;
import com.eldercare.modules.resident_intake.resident.entity.*;
import com.eldercare.modules.resident_intake.resident.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResidentServiceImpl implements ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private AddressRepository addressRepository;

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
        List<Resident> residents = residentRepository.findByIsDeletedFalse();

        // Filter by status
        if (status != null && !status.equalsIgnoreCase("All")) {
            residents = residents.stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        List<ResidentListResponseDto> dtos = residents.stream().map(this::mapToListDto).collect(Collectors.toList());

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

    private ResidentListResponseDto mapToListDto(Resident resident) {
        ResidentListResponseDto dto = new ResidentListResponseDto();
        dto.setId(resident.getId());
        dto.setName(resident.getFirstName() + " " + resident.getLastName());
        
        // Room mapping
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
        List<ResidentInsurancePolicy> policies = residentInsurancePolicyRepository.findByResidentIdAndIsDeletedFalse(resident.getId());
        Optional<ResidentInsurancePolicy> primaryPolicy = policies.stream().filter(ResidentInsurancePolicy::isPrimary).findFirst();
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
        if (id == null) return "Private";
        int index = (int) (id % 5);
        switch (index) {
            case 1: return "Sunrise Regional Hosp.";
            case 2: return "Private";
            case 3: return "Family";
            case 4: return "Self";
            case 0: return "Valley General Hosp.";
            default: return "Private";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentDetailResponseDto getResidentDetail(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        ResidentDetailResponseDto dto = new ResidentDetailResponseDto();
        dto.setId(resident.getId());
        dto.setName(resident.getFirstName() + " " + resident.getLastName());
        
        // Initials
        String initials = "";
        if (resident.getFirstName() != null && !resident.getFirstName().isEmpty()) initials += resident.getFirstName().charAt(0);
        if (resident.getLastName() != null && !resident.getLastName().isEmpty()) initials += resident.getLastName().charAt(0);
        dto.setInitials(initials.toUpperCase());

        // Room
        String roomStr = "—";
        if (resident.getBed() != null) {
            roomStr = resident.getBed().getRoom().getRoomNumber() + resident.getBed().getBedNumber();
        }
        dto.setRoom(roomStr);

        String statusStr = resident.getStatus();
        if (statusStr != null && !statusStr.isEmpty()) {
            dto.setStatus(Character.toUpperCase(statusStr.charAt(0)) + statusStr.substring(1).toLowerCase());
        } else {
            dto.setStatus("Pending");
        }
        dto.setDob(resident.getDateOfBirth());
        
        int ageVal = 75;
        if (resident.getDateOfBirth() != null) {
            ageVal = Period.between(resident.getDateOfBirth(), LocalDate.now()).getYears();
        }
        dto.setAge(ageVal);

        // Badges
        List<ResidentDetailResponseDto.Badge> badges = new ArrayList<>();
        badges.add(new ResidentDetailResponseDto.Badge(dto.getStatus(), "active"));
        
        // DNR (mocked)
        boolean isDnr = false;
        badges.add(new ResidentDetailResponseDto.Badge(isDnr ? "DNR" : "No DNR", isDnr ? "dnr" : "nodnr"));

        // LOC level
        List<ResidentCareLevelHistory> careHistory = residentCareLevelHistoryRepository.findByResidentId(resident.getId());
        String careLevelName = "Level 3";
        if (!careHistory.isEmpty()) {
            careLevelName = careHistory.get(0).getCareLevel().getLevelName();
        }
        badges.add(new ResidentDetailResponseDto.Badge(careLevelName, "level3"));

        // Payer badge
        List<ResidentInsurancePolicy> policies = residentInsurancePolicyRepository.findByResidentIdAndIsDeletedFalse(resident.getId());
        Optional<ResidentInsurancePolicy> primaryPolicy = policies.stream().filter(ResidentInsurancePolicy::isPrimary).findFirst();
        String payerSource = primaryPolicy.isPresent() ? primaryPolicy.get().getInsuranceProvider().getProviderName() : "Private Pay";
        badges.add(new ResidentDetailResponseDto.Badge(payerSource, "payer"));
        dto.setBadges(badges);

        // Demographics
        ResidentDetailResponseDto.Demographics demo = new ResidentDetailResponseDto.Demographics();
        demo.setLegalName(resident.getFirstName() + " " + (resident.getMiddleName() != null ? resident.getMiddleName() + " " : "") + resident.getLastName());
        
        // Address
        if (resident.getAddress() != null) {
            Address addr = resident.getAddress();
            demo.setAddress(addr.getStreetLine1() + (addr.getStreetLine2() != null ? ", " + addr.getStreetLine2() : "") + ", " + addr.getCity() + ", " + addr.getState());
        } else {
            demo.setAddress("—");
        }
        demo.setDob(resident.getDateOfBirth());

        // Admission Date
        List<Admission> admissions = admissionRepository.findByResidentId(resident.getId());
        if (!admissions.isEmpty()) {
            demo.setAdmissionDate(admissions.get(0).getAdmissionDate());
        } else {
            demo.setAdmissionDate(LocalDate.now().minusYears(1));
        }

        // SSN from sensitive info
        Optional<ResidentSensitiveInfo> sensitive = residentSensitiveInfoRepository.findByResidentId(resident.getId());
        demo.setSsn(sensitive.isPresent() ? sensitive.get().getSsnEncrypted() : "XXX-XX-0000");

        demo.setRoomBed(resident.getBed() != null ? resident.getBed().getRoom().getRoomNumber() + " / " + resident.getBed().getBedNumber() : "—");
        demo.setGender(resident.getGender());
        demo.setReferralSource(getMockReferralSource(resident.getId()));
        demo.setMaritalStatus(resident.getMaritalStatus());

        // Contacts / Emergency Contact / POA
        List<ResidentContact> residentContacts = residentContactRepository.findByResidentId(resident.getId());
        Optional<ResidentContact> emergencyContact = residentContacts.stream().filter(ResidentContact::isEmergencyContact).findFirst();
        
        if (emergencyContact.isPresent()) {
            Contact contact = emergencyContact.get().getContact();
            demo.setEmergencyContact(contact.getFirstName() + " " + contact.getLastName() + " (" + emergencyContact.get().getRelationshipType() + ")");
            demo.setPhone(contact.getPhonePrimary());
        } else {
            demo.setEmergencyContact("—");
            demo.setPhone("—");
        }
        demo.setPayerSource(payerSource);
        dto.setDemographics(demo);

        // POA mapping
        ResidentDetailResponseDto.Poa poaDto = new ResidentDetailResponseDto.Poa();
        Optional<ResidentContact> poaContact = residentContacts.stream()
                .filter(rc -> rc.getRelationshipType().equalsIgnoreCase("SON") || 
                        rc.getRelationshipType().equalsIgnoreCase("DAUGHTER") || 
                        rc.getRelationshipType().equalsIgnoreCase("SPOUSE") ||
                        rc.getRelationshipType().equalsIgnoreCase("LEGAL_GUARDIAN"))
                .findFirst();

        if (poaContact.isPresent()) {
            Contact contact = poaContact.get().getContact();
            poaDto.setName(contact.getFirstName() + " " + contact.getLastName());
            poaDto.setRelationship(poaContact.get().getRelationshipType());
            poaDto.setContact(contact.getPhonePrimary());
        } else {
            poaDto.setName("—");
            poaDto.setRelationship("—");
            poaDto.setContact("—");
        }
        poaDto.setDnrFlag(isDnr ? "Yes" : "No");
        dto.setPoa(poaDto);

        // Diagnoses (Clinical records)
        List<ClinicalRecord> diagnosesRecords = clinicalRecordRepository.findByResidentIdAndRecordTypeAndIsDeletedFalse(resident.getId(), "DIAGNOSIS");
        if (!diagnosesRecords.isEmpty()) {
            dto.setDiagnoses(diagnosesRecords.stream().map(ClinicalRecord::getDescription).collect(Collectors.toList()));
        } else {
            dto.setDiagnoses(Arrays.asList("Type 2 DM (E11.9)", "HTN (I10)", "CKD Stage 3 (N18.3)"));
        }

        // Allergies (Clinical records)
        List<ClinicalRecord> allergiesRecords = clinicalRecordRepository.findByResidentIdAndRecordTypeAndIsDeletedFalse(resident.getId(), "ALLERGY");
        if (!allergiesRecords.isEmpty()) {
            dto.setAllergies(allergiesRecords.stream().map(ClinicalRecord::getDescription).collect(Collectors.toList()));
        } else {
            dto.setAllergies(Arrays.asList("Penicillin", "Sulfa drugs", "Latex"));
        }

        // Insurance
        ResidentDetailResponseDto.Insurance insDto = new ResidentDetailResponseDto.Insurance();
        if (primaryPolicy.isPresent()) {
            ResidentInsurancePolicy policy = primaryPolicy.get();
            insDto.setMedicareNum(policy.getPolicyNumberEncrypted());
            insDto.setProvider(policy.getInsuranceProvider().getProviderName());
            insDto.setPayerName(policy.getInsuranceProvider().getProviderName() + " CA");
            insDto.setAuthNum("AUTH-" + policy.getGroupNumber());
            insDto.setAuthStartEnd(policy.getEffectiveFrom() + " / " + (policy.getEffectiveTo() != null ? policy.getEffectiveTo() : "Present"));
        } else {
            insDto.setMedicareNum("—");
            insDto.setProvider("Private Pay");
            insDto.setPayerName("Self Pay");
            insDto.setAuthNum("—");
            insDto.setAuthStartEnd("—");
        }
        dto.setInsurance(insDto);

        // LOC Summary
        ResidentDetailResponseDto.LocSummary loc = new ResidentDetailResponseDto.LocSummary();
        loc.setLevel(careLevelName);
        loc.setAdlScore("20 / 32 (Tier 3)");
        dto.setLocSummary(loc);

        return dto;
    }

    @Override
    public void saveResident(Long id, ResidentSaveRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        updateResidentFields(resident, dto);
        residentRepository.save(resident);
    }

    @Override
    public void createResident(ResidentSaveRequestDto dto) {
        Resident resident = new Resident();
        updateResidentFields(resident, dto);
        resident = residentRepository.save(resident);

        // Save Sensitive info (SSN)
        ResidentSensitiveInfo sensitive = new ResidentSensitiveInfo();
        sensitive.setResident(resident);
        sensitive.setSsnEncrypted(dto.getSsn() != null ? dto.getSsn() : "XXX-XX-9999");
        sensitive.setMedicalRecordNumberEncrypted("MRN-" + resident.getId());
        residentSensitiveInfoRepository.save(sensitive);

        // Save Admission record
        Admission admission = new Admission();
        admission.setResident(resident);
        admission.setAdmissionDate(LocalDate.now());
        admission.setFacilityId(1L);
        admissionRepository.save(admission);

        // Save Care Level history
        CareLevel careLevel = careLevelRepository.findByLevelNameAndIsDeletedFalse("Level 3")
                .orElseGet(() -> {
                    CareLevel cl = new CareLevel();
                    cl.setLevelCode("L3");
                    cl.setLevelName("Level 3");
                    return careLevelRepository.save(cl);
                });
        
        ResidentCareLevelHistory history = new ResidentCareLevelHistory();
        history.setResident(resident);
        history.setCareLevel(careLevel);
        history.setStartDate(LocalDate.now());
        residentCareLevelHistoryRepository.save(history);
    }

    private void updateResidentFields(Resident resident, ResidentSaveRequestDto dto) {
        resident.setFirstName(dto.getFirstName());
        resident.setLastName(dto.getLastName());
        resident.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PENDING");
        resident.setDateOfBirth(dto.getDob());
        resident.setGender(dto.getGender());
        resident.setMaritalStatus(dto.getMaritalStatus());
        resident.setUpdatedAt(OffsetDateTime.now());

        // Address mapping
        if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
            Address address = resident.getAddress();
            if (address == null) {
                address = new Address();
                address.setAddressType("HOME");
            }
            address.setStreetLine1(dto.getAddress());
            address.setCity("Riverside");
            address.setState("CA");
            address.setZipCode("92501");
            address = addressRepository.save(address);
            resident.setAddress(address);
        }

        // Room and Bed mapping (e.g. "106-A" or "106" -> room 106, bed A)
        String roomBed = dto.getReferringFacility(); // UI puts room in referringFacility or maps room directly
        if (roomBed == null || roomBed.isEmpty()) {
            roomBed = "101-A";
        }
        
        String[] parts = roomBed.split("-");
        String roomNumber = parts[0];
        String bedLetter = parts.length > 1 ? parts[1] : "A";

        Room room = roomRepository.findByRoomNumberAndIsDeletedFalse(roomNumber)
                .orElseGet(() -> {
                    Room r = new Room();
                    r.setRoomNumber(roomNumber);
                    r.setRoomType("SEMI_PRIVATE");
                    r.setFacilityId(1L);
                    return roomRepository.save(r);
                });

        Bed bed = bedRepository.findByBedNumberAndRoomId(bedLetter, room.getId())
                .orElseGet(() -> {
                    Bed b = new Bed();
                    b.setBedNumber(bedLetter);
                    b.setStatus("OCCUPIED");
                    b.setRoom(room);
                    return bedRepository.save(b);
                });

        resident.setBed(bed);

        // Insurance mapping
        if (dto.getPayerSource() != null) {
            String providerName = dto.getPayerSource();
            InsuranceProvider provider = insuranceProviderRepository.findByProviderName(providerName)
                    .orElseGet(() -> {
                        InsuranceProvider p = new InsuranceProvider();
                        p.setProviderName(providerName);
                        p.setProviderType(dto.getPayerType() != null ? dto.getPayerType().toUpperCase() : "PRIVATE");
                        return insuranceProviderRepository.save(p);
                    });

            List<ResidentInsurancePolicy> policies = resident.getId() != null ? 
                residentInsurancePolicyRepository.findByResidentIdAndIsDeletedFalse(resident.getId()) : Collections.emptyList();
            
            ResidentInsurancePolicy policy = policies.stream().filter(ResidentInsurancePolicy::isPrimary).findFirst().orElse(null);
            if (policy == null) {
                policy = new ResidentInsurancePolicy();
                policy.setResident(resident);
                policy.setPrimary(true);
            }
            policy.setInsuranceProvider(provider);
            policy.setPolicyNumberEncrypted(dto.getMedicareNum() != null ? dto.getMedicareNum() : "ENC-MCR-000000");
            policy.setEffectiveFrom(LocalDate.now());
            residentInsurancePolicyRepository.save(policy);
        }

        // Emergency Contact
        if (dto.getEmergencyContact() != null) {
            String ecName = dto.getEmergencyContact();
            String[] nameParts = ecName.split(" ");
            String ecFirst = nameParts[0];
            String ecLast = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "Contact";

            Contact contact = new Contact();
            contact.setFirstName(ecFirst);
            contact.setLastName(ecLast);
            contact.setPhonePrimary(dto.getEmergencyPhone() != null ? dto.getEmergencyPhone() : "555-000-0000");
            contact = contactRepository.save(contact);

            ResidentContact rc = new ResidentContact();
            rc.setResident(resident);
            rc.setContact(contact);
            rc.setRelationshipType(dto.getPoaRelationship() != null ? dto.getPoaRelationship().toUpperCase() : "DAUGHTER");
            rc.setEmergencyContact(true);
            rc.setPrimary(true);
            residentContactRepository.save(rc);
        }
    }

    @Override
    public void deleteResident(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));
        resident.setDeleted(true);
        resident.setStatus("DISCHARGED");
        resident.setDeletedAt(OffsetDateTime.now());
        residentRepository.save(resident);
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentListResponseContainerDto getResidentsV1(String status, Long bedId, String search, Integer page, Integer pageSize) {
        List<Resident> residents = residentRepository.findByIsDeletedFalse();

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

        List<Resident> paginatedResidents;
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
        List<ResidentCareLevelHistory> histories = residentCareLevelHistoryRepository.findByResidentId(residentId);
        LocalDate today = LocalDate.now();
        Optional<ResidentCareLevelHistory> activeHistory = histories.stream()
                .filter(h -> !h.getStartDate().isAfter(today) && (h.getEndDate() == null || !h.getEndDate().isBefore(today)))
                .findFirst();
        if (activeHistory.isPresent()) {
            return activeHistory.get().getCareLevel().getLevelCode();
        }
        // Fallback: most recent by startDate DESC
        return histories.stream()
                .sorted(Comparator.comparing(ResidentCareLevelHistory::getStartDate).reversed())
                .map(h -> h.getCareLevel().getLevelCode())
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentResponseDto getResidentByIdV1(Long id) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto createResidentV1(ResidentCreateRequestDto dto) {
        Resident resident = new Resident();
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
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto updateResidentV1(Long id, ResidentUpdateRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        if (resident.isChartLocked()) {
            throw new IllegalStateException("All clinical modifications are blocked because this resident's chart is locked.");
        }

        if (dto.getFirstName() != null) resident.setFirstName(dto.getFirstName());
        if (dto.getMiddleName() != null) resident.setMiddleName(dto.getMiddleName());
        if (dto.getLastName() != null) resident.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null) resident.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) resident.setGender(dto.getGender());
        if (dto.getMaritalStatus() != null) resident.setMaritalStatus(dto.getMaritalStatus());
        if (dto.getReligionPreference() != null) resident.setReligionPreference(dto.getReligionPreference());
        if (dto.getAddressId() != null) {
            addressRepository.findById(dto.getAddressId()).ifPresent(resident::setAddress);
        }
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto updateResidentStatusV1(Long id, ResidentStatusUpdateRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

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
            System.out.println("Audit Log - Resident ID: " + id + " Status change reason: " + dto.getReason());
        }

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto assignResidentBedV1(Long id, ResidentAssignBedRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        if (resident.isChartLocked()) {
            throw new IllegalStateException("All clinical modifications are blocked because this resident's chart is locked.");
        }

        if (dto.getBedId() == null) {
            resident.setBed(null);
        } else {
            Bed bed = bedRepository.findById(dto.getBedId())
                    .orElseThrow(() -> new IllegalArgumentException("Bed not found with id: " + dto.getBedId()));
            
            // Check if bed is already occupied by another active/non-discharged resident
            List<Resident> occupants = residentRepository.findByIsDeletedFalse();
            boolean occupied = occupants.stream()
                    .filter(r -> !r.getId().equals(id))
                    .filter(r -> r.getBed() != null && r.getBed().getId().equals(dto.getBedId()))
                    .anyMatch(r -> !r.getStatus().equalsIgnoreCase("DISCHARGED") && !r.getStatus().equalsIgnoreCase("DECEASED"));
            
            if (occupied) {
                throw new IllegalStateException("Bed is already occupied by another active resident.");
            }
            resident.setBed(bed);
        }
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto lockResidentChartV1(Long id, ResidentChartLockRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("reason is required for audit logging");
        }

        System.out.println("Audit Log - Resident ID: " + id + " locked chart. Reason: " + dto.getReason());
        resident.setChartLocked(true);
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    @Override
    public ResidentResponseDto unlockResidentChartV1(Long id, ResidentChartLockRequestDto dto) {
        Resident resident = residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + id));

        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("reason is required for audit logging");
        }

        System.out.println("Audit Log - Resident ID: " + id + " unlocked chart. Reason: " + dto.getReason());
        resident.setChartLocked(false);
        resident.setUpdatedAt(OffsetDateTime.now());

        resident = residentRepository.save(resident);
        return mapToResponseDto(resident);
    }

    private ResidentResponseDto mapToResponseDto(Resident resident) {
        return ResidentResponseDto.builder()
                .id(resident.getId())
                .firstName(resident.getFirstName())
                .middleName(resident.getMiddleName())
                .lastName(resident.getLastName())
                .dateOfBirth(resident.getDateOfBirth())
                .gender(resident.getGender())
                .status(resident.getStatus())
                .bedId(resident.getBed() != null ? resident.getBed().getId() : null)
                .isChartLocked(resident.isChartLocked())
                .build();
    }
}
