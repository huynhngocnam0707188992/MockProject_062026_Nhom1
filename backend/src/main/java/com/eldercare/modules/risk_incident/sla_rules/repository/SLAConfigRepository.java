package com.eldercare.modules.risk_incident.sla_rules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eldercare.modules.risk_incident.sla_rules.entity.SLAConfig;

@Repository
public interface SLAConfigRepository extends JpaRepository<SLAConfig, Long> {

}
