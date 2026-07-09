package com.eldercare.modules.risk_incident.incident_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eldercare.modules.risk_incident.incident_tracking.entity.IncidentSeverity;

@Repository
public interface IncidentSeverityRepository extends JpaRepository<IncidentSeverity, Long> {

}
