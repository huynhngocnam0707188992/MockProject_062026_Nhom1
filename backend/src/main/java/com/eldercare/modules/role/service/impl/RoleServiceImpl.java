package com.eldercare.modules.role.service.impl;

import com.eldercare.modules.role.dto.response.RoleResponse;
import com.eldercare.modules.role.repository.RoleRepository;
import com.eldercare.modules.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getRoleList() {
        return roleRepository.findByIsDeletedFalse()
                .stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .isDeleted(role.getIsDeleted())
                        .build())
                .toList();
    }
}