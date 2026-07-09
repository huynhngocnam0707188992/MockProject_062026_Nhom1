package com.eldercare.modules.role.controller;

import com.eldercare.modules.role.dto.response.RoleResponse;
import com.eldercare.modules.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getRoleList() {
        return ResponseEntity.ok(roleService.getRoleList());
    }
}