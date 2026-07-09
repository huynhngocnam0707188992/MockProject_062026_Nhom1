package com.eldercare.modules.auth.controller;

import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.admin.user_management.UserRepository;
import com.eldercare.modules.auth.dto.request.LoginRequest;
import com.eldercare.modules.auth.dto.response.LoginResponse;
import com.eldercare.modules.security.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionStore sessionStore;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Username and password are required"));
        }

        // Find user by email
        Optional<UserEntity> userOpt = userRepository.findByEmailAndIsDeletedFalse(username.trim());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials. User not found."));
        }

        UserEntity user = userOpt.get();

        // Check if user status is ACTIVE
        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Account is currently " + user.getStatus()));
        }

        // Normalize BCrypt hash (handles double '$$' typo and '$2b$' compatibility)
        String storedHash = user.getPasswordHash();
        if (storedHash != null) {
            if (storedHash.startsWith("$$")) {
                storedHash = storedHash.substring(1);
            }
            storedHash = storedHash.replace("$2b$", "$2a$");
        }

        // Verify password
        if (!passwordEncoder.matches(password, storedHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials. Please try again."));
        }

        // Update last login time
        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        String fullName = user.getFirstName() + (user.getMiddleName() != null ? " " + user.getMiddleName() : "") + " " + user.getLastName();
        String token = "mock-jwt-token-" + UUID.randomUUID().toString().substring(0, 8);
        String sessionId = "sess_" + UUID.randomUUID().toString().substring(0, 6);

        sessionStore.createSession(token, sessionId, user.getId(), user.getEmail(), user.getRole().getRoleName(), fullName);

        return ResponseEntity.ok(new LoginResponse(
            token,
            sessionId,
            user.getRole().getRoleName(),
            fullName
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Email is required"));
        }

        Optional<UserEntity> userOpt = userRepository.findByEmailAndIsDeletedFalse(email.trim());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Account email does not exist in the database"));
        }

        return ResponseEntity.ok(Map.of("message", "Password reset link sent to email"));
    }
}
