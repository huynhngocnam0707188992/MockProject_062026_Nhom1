package com.eldercare.modules.resident_intake.resident.repository;


import com.eldercare.modules.resident_intake.family_contacts.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
