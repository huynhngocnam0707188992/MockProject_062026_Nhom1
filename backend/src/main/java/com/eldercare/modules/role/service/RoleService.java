package com.eldercare.modules.role.service;

import com.eldercare.modules.role.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getRoleList();

}