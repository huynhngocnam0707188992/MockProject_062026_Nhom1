package com.eldercare.modules.risk_incident.sla_rules.service.impl;

import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverity;
import com.eldercare.modules.risk_incident.incident_tracking.repository.IncidentSeverityRepository;
import com.eldercare.modules.risk_incident.sla_rules.dto.CreateSLARequest;
import com.eldercare.modules.risk_incident.sla_rules.dto.SLAResponse;
import com.eldercare.modules.risk_incident.sla_rules.dto.UpdateSLARequest;
import com.eldercare.modules.risk_incident.sla_rules.entity.SLAConfig;
import com.eldercare.modules.risk_incident.sla_rules.mapper.SLAConfigMapper;
import com.eldercare.modules.risk_incident.sla_rules.repository.SLAConfigRepository;
import com.eldercare.modules.risk_incident.sla_rules.service.SLAConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SLAConfigServiceImpl implements SLAConfigService {

    private final SLAConfigRepository slaConfigRepository;
    private final IncidentSeverityRepository incidentSeverityRepository;

    @Override
    public List<SLAResponse> getAllSLAConfigs() {
        return slaConfigRepository.findAll().stream()
                .map(SLAConfigMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SLAResponse createSLAConfig(CreateSLARequest request) {
        IncidentSeverity severity = incidentSeverityRepository.findById(request.getSeverityId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident severity not found"));

        SLAConfig saved = slaConfigRepository.save(SLAConfigMapper.toEntity(request, severity));
        return SLAConfigMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SLAResponse updateSLAConfig(Long slaConfigId, UpdateSLARequest request) {
        SLAConfig existing = slaConfigRepository.findById(slaConfigId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SLA config not found"));

        IncidentSeverity severity = incidentSeverityRepository.findById(request.getSeverityId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident severity not found"));

        SLAConfigMapper.updateEntity(existing, request, severity);
        return SLAConfigMapper.toResponse(slaConfigRepository.save(existing));
    }
}
