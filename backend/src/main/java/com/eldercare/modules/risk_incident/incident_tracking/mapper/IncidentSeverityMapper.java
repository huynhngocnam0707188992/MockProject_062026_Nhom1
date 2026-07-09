package com.eldercare.modules.risk_incident.incident_tracking.mapper;

import com.eldercare.modules.risk_incident.incident_tracking.dto.CreateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.dto.IncidentSeverityResponse;
import com.eldercare.modules.risk_incident.incident_tracking.dto.UpdateIncidentSeverityRequest;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverityEntity;

public interface IncidentSeverityMapper {

    static IncidentSeverityResponse toResponse(IncidentSeverityEntity incidentSeverity) {
        if (incidentSeverity == null) {
            return null;
        }

        return IncidentSeverityResponse.builder()
                .id(incidentSeverity.getId())
                .levelName(incidentSeverity.getLevelName())
                .chartLockTrigger(incidentSeverity.getChartLockTrigger())
                .build();
    }

    static IncidentSeverityEntity toEntity(CreateIncidentSeverityRequest request) {
        if (request == null) {
            return null;
        }

        return IncidentSeverityEntity.builder()
                .levelName(request.getLevelName())
                .chartLockTrigger(request.getChartLockTrigger())
                .build();
    }

    static void updateEntity(IncidentSeverityEntity target, UpdateIncidentSeverityRequest request) {
        if (target == null || request == null) {
            return;
        }

        target.setLevelName(request.getLevelName());
        target.setChartLockTrigger(request.getChartLockTrigger());
    }
}
