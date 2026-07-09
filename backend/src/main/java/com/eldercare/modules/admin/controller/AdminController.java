package com.eldercare.modules.admin.controller;

import com.eldercare.modules.admin.security_telemetry.hipaa_audit_trail.AuditLogEntity;
import com.eldercare.modules.admin.security_telemetry.hipaa_audit_trail.PhiAccessLogEntity;
import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.admin.security_telemetry.hipaa_audit_trail.AuditLogRepository;
import com.eldercare.modules.admin.security_telemetry.hipaa_audit_trail.PhiAccessLogRepository;
import com.eldercare.modules.admin.user_management.UserRepository;
import com.eldercare.modules.security.SessionDetails;
import com.eldercare.modules.security.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private PhiAccessLogRepository phiAccessLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionStore sessionStore;

    // 0. Get Real Users list
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> users = userRepository.findAllByIsDeletedFalse();
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (UserEntity user : users) {
            responseList.add(Map.of(
                "id", user.getId(),
                "name", user.getFirstName() + " " + user.getLastName(),
                "email", user.getEmail(),
                "status", user.getStatus()
            ));
        }
        return ResponseEntity.ok(responseList);
    }

    // 1. Audit Logs (System Actions)
    @GetMapping("/audit-logs")
    public ResponseEntity<?> getAuditLogs(
            @RequestParam(required = false) String table_name,
            @RequestParam(required = false) String record_id,
            @RequestParam(required = false) Long performed_by,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        
        org.springframework.data.jpa.domain.Specification<AuditLogEntity> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (table_name != null && !table_name.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("tableName"), table_name.trim()));
            }
            if (record_id != null && !record_id.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("recordId"), record_id.trim()));
            }
            if (performed_by != null) {
                predicates.add(cb.equal(root.get("performedBy"), performed_by));
            }
            if (action != null && !action.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("action"), action.trim()));
            }
            if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                try {
                    OffsetDateTime from = OffsetDateTime.parse(dateFrom.trim());
                    predicates.add(cb.greaterThanOrEqualTo(root.get("performedAt"), from));
                } catch (Exception e) {}
            }
            if (dateTo != null && !dateTo.trim().isEmpty()) {
                try {
                    OffsetDateTime to = OffsetDateTime.parse(dateTo.trim());
                    predicates.add(cb.lessThanOrEqualTo(root.get("performedAt"), to));
                } catch (Exception e) {}
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        int jpaPage = Math.max(0, page - 1);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(jpaPage, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "performedAt"));
        
        org.springframework.data.domain.Page<AuditLogEntity> resultPage = auditLogRepository.findAll(spec, pageable);

        Map<String, Object> pagination = Map.of(
            "page", page,
            "pageSize", pageSize,
            "totalItems", resultPage.getTotalElements(),
            "totalPages", Math.max(1, resultPage.getTotalPages())
        );

        return ResponseEntity.ok(Map.of("data", resultPage.getContent(), "page", pagination));
    }

    // 2. PHI Access Logs
    @GetMapping("/phi-access-logs")
    public ResponseEntity<?> getPHIAccessLogs(
            @RequestParam(required = false) String table_name,
            @RequestParam(required = false) String record_id,
            @RequestParam(required = false) Long accessed_by,
            @RequestParam(required = false) String access_type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        
        org.springframework.data.jpa.domain.Specification<PhiAccessLogEntity> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (table_name != null && !table_name.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("tableName"), table_name.trim()));
            }
            if (record_id != null && !record_id.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("recordId"), record_id.trim()));
            }
            if (accessed_by != null) {
                predicates.add(cb.equal(root.get("accessedBy"), accessed_by));
            }
            if (access_type != null && !access_type.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("accessType"), access_type.trim()));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        int jpaPage = Math.max(0, page - 1);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(jpaPage, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "accessedAt"));
        
        org.springframework.data.domain.Page<PhiAccessLogEntity> resultPage = phiAccessLogRepository.findAll(spec, pageable);

        Map<String, Object> pagination = Map.of(
            "page", page,
            "pageSize", pageSize,
            "totalItems", resultPage.getTotalElements(),
            "totalPages", Math.max(1, resultPage.getTotalPages())
        );

        return ResponseEntity.ok(Map.of("data", resultPage.getContent(), "page", pagination));
    }

    // 3. Active Sessions
    @GetMapping("/sessions")
    public ResponseEntity<?> getActiveSessions() {
        List<SessionDetails> sessions = sessionStore.getAllSessions();
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (SessionDetails details : sessions) {
            responseList.add(Map.of(
                "session_id", details.getSessionId(),
                "user_id", details.getUserId(),
                "login_at", details.getLoginAt().toString(),
                "last_activity_at", details.getLastActivityAt().toString(),
                "status", details.getStatus()
            ));
        }
        return ResponseEntity.ok(responseList);
    }

    // 4. Force Logout
    @PostMapping("/sessions/{sessionId}/force-logout")
    public ResponseEntity<?> forceLogout(@PathVariable String sessionId) {
        boolean found = sessionStore.forceLogout(sessionId);
        if (!found) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Session not found or already logged out"));
        }
        return ResponseEntity.ok(Map.of(
            "session_id", sessionId,
            "status", "ForcedLogout"
        ));
    }

    // 5. Change User Status
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        String newStatus = body.getOrDefault("status", "INACTIVE");
        
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "User not found"));
        }
        
        UserEntity user = userOpt.get();
        user.setStatus(newStatus.toUpperCase());
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);
        
        return ResponseEntity.ok(Map.of(
            "id", userId,
            "status", user.getStatus(),
            "updated_at", user.getUpdatedAt().toString()
        ));
    }
}
