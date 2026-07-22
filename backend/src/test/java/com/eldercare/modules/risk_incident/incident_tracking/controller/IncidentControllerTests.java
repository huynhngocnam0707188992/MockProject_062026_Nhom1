package com.eldercare.modules.risk_incident.incident_tracking.controller;

import com.eldercare.modules.risk_incident.incident_tracking.dto.reponse.IncidentResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.request.CreateIncidentRequest;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentType;
import com.eldercare.modules.risk_incident.incident_tracking.service.IncidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IncidentControllerTests {

    // Path gốc phải khớp @RequestMapping của IncidentController
    private static final String BASE_URL = "/api/v1/admin/incidents";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentController incidentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(incidentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    // ---------- getListIncidents (GET /api/v1/admin/incidents) ----------

    @Test
    void getIncidents_shouldReturn200AndPageOfIncidents() throws Exception {
        IncidentResponse res = IncidentResponse.builder()
                .id(1L)
                .incidentType("FALL")
                .status("OPEN")
                .build();
        Page<IncidentResponse> page = new PageImpl<>(List.of(res), PageRequest.of(0, 20), 1);

        when(incidentService.getIncidents(any())).thenReturn(page);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].status").value("OPEN"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(incidentService, times(1)).getIncidents(any());
    }


    // ---------- getDetailOneIncident (GET /api/v1/admin/incidents/{id}) ----------

    @Test
    void getIncidentById_shouldReturn200AndFullDetail_whenExists() throws Exception {
        IncidentResponse res = IncidentResponse.builder()
                .id(10L)
                .incidentType("FALL")
                .status("OPEN")
                .slaDeadlineHours(24)
                .build();

        when(incidentService.getIncidentById(10L)).thenReturn(res);

        mockMvc.perform(get(BASE_URL + "/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.slaDeadlineHours").value(24));
    }

    @Test
    void getIncidentById_shouldPropagateNotFound_whenIncidentMissing() {
        when(incidentService.getIncidentById(999L))
                .thenThrow(new RuntimeException("Incident not found: 999"));

        ServletException ex = assertThrows(ServletException.class, () ->
                mockMvc.perform(get(BASE_URL + "/{id}", 999)));

        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertEquals("Incident not found: 999", ex.getCause().getMessage());
    }

    // ---------- createOneIncident (POST /api/v1/admin/incidents) ----------

    @Test
    void createIncident_shouldReturn201_whenRequestValid() throws Exception {
        CreateIncidentRequest request = new CreateIncidentRequest(
                1L, IncidentType.FALL, 2L,
                LocalDateTime.of(2026, 7, 20, 9, 0, 0),
                "Room 101", "Resident fell in bathroom", "Nurse A"
        );

        IncidentResponse response = IncidentResponse.builder()
                .id(100L)
                .status("OPEN")
                .isLocked(true)
                .build();

        when(incidentService.createIncident(any(CreateIncidentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.isLocked").value(true));
    }

    @Test
    void createIncident_shouldReturn400_whenResidentIdMissing() throws Exception {
        String invalidJson = """
                {
                  "incidentType": "FALL",
                  "severityID": 2,
                  "occurredAt": "2026-07-20 09:00:00"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(incidentService, never()).createIncident(any());
    }
}