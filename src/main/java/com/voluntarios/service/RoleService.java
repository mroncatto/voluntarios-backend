package com.voluntarios.service;

import com.voluntarios.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    Role findById(Long id);
}
