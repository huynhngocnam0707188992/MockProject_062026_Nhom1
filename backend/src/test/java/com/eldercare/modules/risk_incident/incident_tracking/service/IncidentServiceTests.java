package com.eldercare.modules.risk_incident.incident_tracking.service;

import com.eldercare.exception.NotFoundException;
import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.admin.user_management.UserRepository;
import com.eldercare.modules.resident_intake.resident.repository.ResidentRepository;
import com.eldercare.modules.resident_intake.resident_profile.ResidentEntity;
import com.eldercare.modules.risk_incident.incident_timeline.entity.IncidentTimelineEntity;
import com.eldercare.modules.risk_incident.incident_timeline.repository.IncidentTimelineRepository;
import com.eldercare.modules.risk_incident.incident_tracking.dto.reponse.IncidentResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.request.CreateIncidentRequest;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentEntity;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverityEntity;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentStatus;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentType;
import com.eldercare.modules.risk_incident.incident_tracking.mapper.IncidentMapper;
import com.eldercare.modules.risk_incident.incident_tracking.repository.IncidentRepository;
import com.eldercare.modules.risk_incident.incident_tracking.repository.IncidentSeverityRepository;
import com.eldercare.modules.risk_incident.incident_tracking.service.impl.IncidentServiceImpl;
import com.eldercare.modules.risk_incident.sla_rules.entity.SLAConfigEntity;
import com.eldercare.modules.risk_incident.sla_rules.repository.SLAConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTests {

    @Mock private IncidentRepository incidentRepository;
    @Mock private IncidentSeverityRepository severityRepository;
    @Mock private SLAConfigRepository slaConfigRepository;
    @Mock private ResidentRepository residentRepository;
    @Mock private UserRepository userRepository;
    @Mock private IncidentTimelineRepository timelineRepository;
    @Mock private IncidentMapper mapper;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    private ResidentEntity resident;
    private UserEntity reporter;
    private IncidentSeverityEntity severity;
    private CreateIncidentRequest request;

    @BeforeEach
    void setUp() {
        // Dùng mock() thay vì builder() thật vì ta không quan tâm field nội bộ
        // của Resident/User — chỉ cần verify hành vi (setChartLocked, save...) được gọi đúng.
        resident = mock(ResidentEntity.class);
        reporter = mock(UserEntity.class);

        severity = IncidentSeverityEntity.builder()
                .id(2L)
                .levelName("Critical")
                .chartLockTrigger(true)
                .build();

        request = new CreateIncidentRequest(
                1L,                                  // residentID
                IncidentType.FALL,                   // incidentType
                2L,                                  // severityID
                LocalDateTime.of(2026, 7, 20, 9, 0),  // occurredAt
                "Room 101",
                "Resident fell in bathroom",
                "Nurse A"
        );
    }

    // ---------------- createIncident ----------------

    @Test
    @DisplayName("Create incident - Success, severity yêu cầu lock chart -> resident bị khóa")
    void createIncident_Success_LocksChart() {
        when(residentRepository.findById(1L)).thenReturn(Optional.of(resident));
        when(severityRepository.findById(2L)).thenReturn(Optional.of(severity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter)); // currentUserId hardcode = 1L trong service
        when(slaConfigRepository.findBySeverityId(2L))
                .thenReturn(Optional.of(SLAConfigEntity.builder().slaWindowHrs(48).build()));

        when(incidentRepository.save(any(IncidentEntity.class)))
                .thenAnswer(invocation -> {
                    IncidentEntity e = invocation.getArgument(0);
                    e.setId(100L); // giả lập DB gán ID sau khi save
                    return e;
                });

        when(timelineRepository.save(any(IncidentTimelineEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IncidentResponse expectedResponse = IncidentResponse.builder()
                .id(100L)
                .status("OPEN")
                .isLocked(true)
                .build();
        when(mapper.toDetail(any(IncidentEntity.class), eq(48), anyList()))
                .thenReturn(expectedResponse);

        IncidentResponse result = incidentService.createIncident(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("OPEN", result.getStatus());
        assertTrue(result.getIsLocked());

        // Business rule quan trọng nhất: chartLockTrigger=true -> phải gọi setChartLocked(true) + save
        verify(resident).setChartLocked(true);
        verify(residentRepository).save(resident);
        verify(timelineRepository).save(any(IncidentTimelineEntity.class));
        verify(incidentRepository).save(any(IncidentEntity.class));
    }

    @Test
    @DisplayName("Create incident - Severity không yêu cầu lock -> resident KHÔNG bị khóa")
    void createIncident_Success_DoesNotLockChart() {
        IncidentSeverityEntity minorSeverity = IncidentSeverityEntity.builder()
                .id(3L)
                .levelName("Minor")
                .chartLockTrigger(false)
                .build();

        CreateIncidentRequest minorRequest = new CreateIncidentRequest(
                1L, IncidentType.SKIN_TEAR, 3L,
                LocalDateTime.of(2026, 7, 20, 9, 0),
                "Room 102", "Small skin tear", null
        );

        when(residentRepository.findById(1L)).thenReturn(Optional.of(resident));
        when(severityRepository.findById(3L)).thenReturn(Optional.of(minorSeverity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter));
        when(slaConfigRepository.findBySeverityId(3L)).thenReturn(Optional.empty()); // không có config -> default 24h

        when(incidentRepository.save(any(IncidentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(timelineRepository.save(any(IncidentTimelineEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDetail(any(), eq(24), anyList()))
                .thenReturn(IncidentResponse.builder().status("OPEN").isLocked(false).build());

        IncidentResponse result = incidentService.createIncident(minorRequest);

        assertFalse(result.getIsLocked());
        verify(resident, never()).setChartLocked(anyBoolean());
        verify(residentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident - Resident không tồn tại -> NotFoundException")
    void createIncident_ResidentNotFound() {
        when(residentRepository.findById(999L)).thenReturn(Optional.empty());

        CreateIncidentRequest invalidRequest = new CreateIncidentRequest(
                999L, IncidentType.FALL, 2L,
                LocalDateTime.now(), "Room 1", "desc", "witness"
        );

        assertThrows(NotFoundException.class,
                () -> incidentService.createIncident(invalidRequest));

        verify(severityRepository, never()).findById(any());
        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident - Severity không tồn tại -> NotFoundException")
    void createIncident_SeverityNotFound() {
        when(residentRepository.findById(1L)).thenReturn(Optional.of(resident));
        when(severityRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> incidentService.createIncident(request));

        verify(userRepository, never()).findById(any());
        verify(incidentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create incident - Reporter (user) không tồn tại -> NotFoundException")
    void createIncident_ReporterNotFound() {
        when(residentRepository.findById(1L)).thenReturn(Optional.of(resident));
        when(severityRepository.findById(2L)).thenReturn(Optional.of(severity));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> incidentService.createIncident(request));

        verify(incidentRepository, never()).save(any());
    }

    // ---------------- getIncidents ----------------

    @Test
    @DisplayName("Get incidents - Trả về đúng page đã map qua mapper")
    void getIncidents_Success() {
        IncidentEntity entity = IncidentEntity.builder().id(1L).status(IncidentStatus.OPEN).build();
        Pageable pageable = PageRequest.of(0, 20);
        Page<IncidentEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(incidentRepository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toResponse(entity))
                .thenReturn(IncidentResponse.builder().id(1L).status("OPEN").build());

        Page<IncidentResponse> result = incidentService.getIncidents(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("OPEN", result.getContent().get(0).getStatus());
        verify(mapper).toResponse(entity);
    }

    @Test
    @DisplayName("Get incidents - Không có dữ liệu -> trả page rỗng")
    void getIncidents_EmptyPage() {
        Pageable pageable = PageRequest.of(0, 20);
        when(incidentRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        Page<IncidentResponse> result = incidentService.getIncidents(pageable);

        assertTrue(result.isEmpty());
        verify(mapper, never()).toResponse(any());
    }

    // ---------------- getIncidentById ----------------

    @Test
    @DisplayName("Get incident by id - Success, có timeline và SLA config")
    void getIncidentById_Success() {
        IncidentEntity entity = IncidentEntity.builder()
                .id(10L)
                .status(IncidentStatus.OPEN)
                .severity(severity)
                .build();

        List<IncidentTimelineEntity> timelines = List.of(
                IncidentTimelineEntity.builder().id(1L).action("Incident reported").build()
        );

        when(incidentRepository.findWithDetailById(10L)).thenReturn(entity);
        when(slaConfigRepository.findBySeverityId(2L))
                .thenReturn(Optional.of(SLAConfigEntity.builder().slaWindowHrs(24).build()));
        when(timelineRepository.findByIncidentIdOrderByCreatedAtAsc(10L)).thenReturn(timelines);
        when(mapper.toDetail(entity, 24, timelines))
                .thenReturn(IncidentResponse.builder().id(10L).status("OPEN").build());

        IncidentResponse result = incidentService.getIncidentById(10L);

        assertEquals(10L, result.getId());
        verify(timelineRepository).findByIncidentIdOrderByCreatedAtAsc(10L);
    }

    @Test
    @DisplayName("Get incident by id - Không tồn tại -> NotFoundException")
    void getIncidentById_NotFound() {
        when(incidentRepository.findWithDetailById(999L)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> incidentService.getIncidentById(999L));

        verify(slaConfigRepository, never()).findBySeverityId(any());
        verify(timelineRepository, never()).findByIncidentIdOrderByCreatedAtAsc(any());
    }
}