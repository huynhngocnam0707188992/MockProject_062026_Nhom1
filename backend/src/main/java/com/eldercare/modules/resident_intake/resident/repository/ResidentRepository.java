package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long>, JpaSpecificationExecutor<Resident> {
    List<Resident> findByIsDeletedFalse();
}
