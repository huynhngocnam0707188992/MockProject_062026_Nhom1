package com.eldercare.modules.risk_incident.sla_rules.mapper;

import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverity;
import com.eldercare.modules.risk_incident.sla_rules.dto.CreateSLARequest;
import com.eldercare.modules.risk_incident.sla_rules.dto.SLAResponse;
import com.eldercare.modules.risk_incident.sla_rules.dto.UpdateSLARequest;
import com.eldercare.modules.risk_incident.sla_rules.entity.SLAConfig;

public interface SLAConfigMapper {

    static SLAResponse toResponse(SLAConfig slaConfig) {
        if (slaConfig == null) {
            return null;
        }

        return SLAResponse.builder()
                .id(slaConfig.getId())
                .severityId(slaConfig.getSeverity().getId())
                .slaWindowHrs(slaConfig.getSlaWindowHrs())
                .build();
    }

    static SLAConfig toEntity(CreateSLARequest request, IncidentSeverity severity) {
        if (request == null || severity == null) {
            return null;
        }

        return SLAConfig.builder()
                .slaWindowHrs(request.getSlaWindowHrs())
                .severity(severity)
                .build();
    }

    static void updateEntity(SLAConfig target, UpdateSLARequest request, IncidentSeverity severity) {
        if (target == null || request == null || severity == null) {
            return;
        }

        target.setSlaWindowHrs(request.getSlaWindowHrs());
        target.setSeverity(severity);
    }
}
