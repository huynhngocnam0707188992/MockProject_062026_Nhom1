package com.eldercare.modules.resident_intake.resident.repository;


import com.eldercare.modules.resident_intake.family_contacts.ResidentContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResidentContactRepository extends JpaRepository<ResidentContactEntity, Long> {
    List<ResidentContactEntity> findByResidentId(Long residentId);
}
