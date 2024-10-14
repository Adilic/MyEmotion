package com.example.demo.auth.service;

import com.example.demo.auth.DTO.PermissionDTO;
import com.example.demo.auth.DTO.RoleDTO;
import com.example.demo.auth.DTO.UpdateRoleDTO;
import com.example.demo.auth.DTO.UserRoleDTO;
import com.example.demo.auth.model.Permission;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.repository.PermissionRepository;
import com.example.demo.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public List<RoleDTO> getRolesWithPermissions() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOs = new ArrayList<>();

        for (Role role : roles) {
            List<PermissionDTO> permissionDTOs = role.getPermissions().stream()
                    .map(permission -> new PermissionDTO(permission.getId(), permission.getPermissionName()))
                    .collect(Collectors.toList());

            // 现在传递 role.getId()
            roleDTOs.add(new RoleDTO(role.getId(), role.getRoleName(), permissionDTOs));
        }
        return roleDTOs;
    }




    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public boolean deleteRoleById(int id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            roleRepository.delete(role.get());
            return true;
        }
        return false;
    }

    public Optional<Role> findById(int roleId) {
        return roleRepository.findById(roleId);  // 使用 JPA 提供的 findById 方法
    }

    public Role addRole(String roleName, List<Integer> permissionIds) {
        // 创建新角色
        Role role = new Role();
        role.setRoleName(roleName);

        // 查找权限
        Set<Permission> permissions = new HashSet<>();
        for (Integer permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionId));
            permissions.add(permission);
        }

        // 设置角色的权限
        role.setPermissions(permissions);

        // 保存角色
        return roleRepository.save(role);
    }

    // 更新角色和权限
    public void updateRole(int roleId, UpdateRoleDTO updateRoleDTO) {
        // 获取现有角色
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // 更新角色名称
        existingRole.setRoleName(updateRoleDTO.getRoleName());

        // 获取新的权限集合
        Set<Permission> updatedPermissions = new HashSet<>();
        for (int permissionId : updateRoleDTO.getPermissionIds()) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found"));
            updatedPermissions.add(permission);
        }

        // 更新角色的权限
        existingRole.setPermissions(updatedPermissions);

        // 保存更新后的角色
        roleRepository.save(existingRole);
    }

    // 获取所有角色并返回 UserRoleDTO 列表
    public List<UserRoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new UserRoleDTO(role.getId(), role.getRoleName()))
                .collect(Collectors.toList());
    }
}
