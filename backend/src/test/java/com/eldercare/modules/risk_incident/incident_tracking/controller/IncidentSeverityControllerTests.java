package com.eldercare.modules.risk_incident.incident_tracking.controller;

import com.eldercare.modules.risk_incident.incident_tracking.dto.reponse.IncidentSeverityResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.request.UpdateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.service.IncidentSeverityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class IncidentSeverityControllerTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IncidentSeverityService incidentSeverityService;

    @InjectMocks
    private IncidentSeverityController incidentSeverityController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(incidentSeverityController)
                .build();
    }

    // ==========================
    // API 1: GET ALL
    // ==========================

    @Test
    void getAllSeverityLevels_shouldReturn200AndList() throws Exception {

        IncidentSeverityResponse response = IncidentSeverityResponse.builder()
                .id(1L)
                .levelName("Critical")
                .chartLockTrigger(true)
                .description("Critical incident")
                .example("Resident falls")
                .build();

        when(incidentSeverityService.getAllSeverityLevels())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/admin/incident-severity-levels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level_name").value("Critical"))
                .andExpect(jsonPath("$[0].chart_lock_trigger").value(true))
                .andExpect(jsonPath("$[0].description").value("Critical incident"));
    }

    @Test
    void getAllSeverityLevels_shouldReturnEmptyArray_whenNoneConfigured() throws Exception {

        when(incidentSeverityService.getAllSeverityLevels())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/incident-severity-levels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ==========================
    // API 2: UPDATE
    // ==========================

    @Test
    void updateSeverityLevel_shouldReturn200_whenExists() throws Exception {

        UpdateIncidentSeverityRequest request =
                UpdateIncidentSeverityRequest.builder()
                        .levelName("Critical")
                        .chartLockTrigger(true)
                        .description("Mô tả mới")
                        .example("Ví dụ")
                        .build();

        IncidentSeverityResponse response =
                IncidentSeverityResponse.builder()
                        .id(1L)
                        .levelName("Critical")
                        .chartLockTrigger(true)
                        .description("Mô tả mới")
                        .example("Ví dụ")
                        .build();

        when(incidentSeverityService.updateSeverityLevel(
                eq(1L),
                any(UpdateIncidentSeverityRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/admin/incident-severity-levels/{severityId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level_name").value("Critical"))
                .andExpect(jsonPath("$.chart_lock_trigger").value(true))
                .andExpect(jsonPath("$.description").value("Mô tả mới"));
    }

    @Test
    void updateSeverityLevel_shouldReturn404_whenIdNotExists() throws Exception {

        UpdateIncidentSeverityRequest request =
                UpdateIncidentSeverityRequest.builder()
                        .levelName("Critical")
                        .chartLockTrigger(true)
                        .description("Không tồn tại")
                        .example("Ví dụ")
                        .build();

        when(incidentSeverityService.updateSeverityLevel(
                eq(999L),
                any(UpdateIncidentSeverityRequest.class)))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Severity level not found"));

        mockMvc.perform(
                        put("/api/v1/admin/incident-severity-levels/{severityId}", 999L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }
}