package com.eldercare.modules.role.repository;

import com.eldercare.modules.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByIsDeletedFalse();

}