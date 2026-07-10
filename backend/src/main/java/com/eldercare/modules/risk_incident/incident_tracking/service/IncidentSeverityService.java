package com.eldercare.modules.risk_incident.incident_tracking.service;

import com.eldercare.modules.risk_incident.incident_tracking.dto.CreateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.dto.IncidentSeverityResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.UpdateIncidentSeverityRequest;
import java.util.List;

public interface IncidentSeverityService {

    List<IncidentSeverityResponse> getAllSeverityLevels();

    IncidentSeverityResponse createSeverityLevel(CreateIncidentSeverityRequest request);

    IncidentSeverityResponse updateSeverityLevel(Long severityId, UpdateIncidentSeverityRequest request);
}
