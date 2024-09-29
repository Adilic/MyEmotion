package com.example.demo.auth.service;

import com.example.demo.auth.model.Permission;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.repository.PermissionRepository;
import com.example.demo.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role addPermissionToRole(int roleId, int permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow();
        Permission permission = permissionRepository.findById(permissionId).orElseThrow();
        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }
}
