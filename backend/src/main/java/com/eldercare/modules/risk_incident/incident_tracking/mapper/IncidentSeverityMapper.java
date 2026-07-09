package com.eldercare.modules.risk_incident.incident_tracking.mapper;

import com.eldercare.modules.risk_incident.incident_tracking.dto.CreateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.dto.IncidentSeverityResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.UpdateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverity;

public interface IncidentSeverityMapper {

    static IncidentSeverityResponse toResponse(IncidentSeverity incidentSeverity) {
        if (incidentSeverity == null) {
            return null;
        }

        return IncidentSeverityResponse.builder()
                .id(incidentSeverity.getId())
                .levelName(incidentSeverity.getLevelName())
                .chartLockTrigger(incidentSeverity.getChartLockTrigger())
                .build();
    }

    static IncidentSeverity toEntity(CreateIncidentSeverityRequest request) {
        if (request == null) {
            return null;
        }

        return IncidentSeverity.builder()
                .levelName(request.getLevelName())
                .chartLockTrigger(request.getChartLockTrigger())
                .build();
    }

    static void updateEntity(IncidentSeverity target, UpdateIncidentSeverityRequest request) {
        if (target == null || request == null) {
            return;
        }

        target.setLevelName(request.getLevelName());
        target.setChartLockTrigger(request.getChartLockTrigger());
    }
}
