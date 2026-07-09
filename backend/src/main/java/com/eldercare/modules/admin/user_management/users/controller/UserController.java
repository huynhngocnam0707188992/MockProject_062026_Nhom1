package com.eldercare.modules.admin.user_management.users.controller;

import com.eldercare.modules.admin.user_management.users.dto.request.ChangeStatusRequest;
import com.eldercare.modules.admin.user_management.users.dto.request.CreateUserRequest;
import com.eldercare.modules.admin.user_management.users.dto.request.UpdateUserRequest;
import com.eldercare.modules.admin.user_management.users.dto.response.UserDetailResponse;
import com.eldercare.modules.admin.user_management.users.dto.response.UserResponse;
import com.eldercare.modules.admin.user_management.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        Page<UserResponse> users = userService.getUsers(
                keyword,
                roleId,
                status,
                pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUserById(
            @PathVariable Long id) {

        UserDetailResponse user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDetailResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserDetailResponse createdUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserDetailResponse updatedUser = userService.updateUser(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDetailResponse> changeUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request) {

        UserDetailResponse updatedUser = userService.changeUserStatus(id, request);

        return ResponseEntity.ok(updatedUser);
    }
}