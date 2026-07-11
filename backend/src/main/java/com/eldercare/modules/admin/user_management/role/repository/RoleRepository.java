package com.eldercare.modules.admin.user_management.role.repository;

import com.eldercare.modules.admin.user_management.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findByIsDeletedFalse();

}
