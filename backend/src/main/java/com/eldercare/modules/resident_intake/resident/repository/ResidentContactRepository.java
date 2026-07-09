package com.eldercare.modules.resident_intake.resident.repository;

import com.eldercare.modules.resident_intake.resident.entity.ResidentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentContactRepository extends JpaRepository<ResidentContact, Long> {
    List<ResidentContact> findByResidentId(Long residentId);
}
