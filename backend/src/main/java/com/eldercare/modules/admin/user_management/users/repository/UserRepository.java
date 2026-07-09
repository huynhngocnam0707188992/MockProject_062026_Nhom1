package com.eldercare.modules.admin.user_management.users.repository;

import com.eldercare.modules.admin.user_management.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
            SELECT u FROM UserEntity u
            WHERE u.isDeleted = false
            AND (:keyword IS NULL OR u.email LIKE %:keyword% OR u.phoneNumber LIKE %:keyword%)
            AND (:roleId IS NULL OR u.role.id = :roleId)
            AND (:status IS NULL OR u.status = :status)
            ORDER BY u.createdAt DESC
            """)
    Page<UserEntity> search(
            @Param("keyword") String keyword,
            @Param("roleId") Long roleId,
            @Param("status") String status,
            Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}