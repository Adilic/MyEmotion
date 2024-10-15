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
        return roleRepository.findById(roleId);
    }

    public Role addRole(String roleName, List<Integer> permissionIds) {

        Role role = new Role();
        role.setRoleName(roleName);


        Set<Permission> permissions = new HashSet<>();
        for (Integer permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionId));
            permissions.add(permission);
        }


        role.setPermissions(permissions);


        return roleRepository.save(role);
    }


    public void updateRole(int roleId, UpdateRoleDTO updateRoleDTO) {

        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));


        existingRole.setRoleName(updateRoleDTO.getRoleName());


        Set<Permission> updatedPermissions = new HashSet<>();
        for (int permissionId : updateRoleDTO.getPermissionIds()) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found"));
            updatedPermissions.add(permission);
        }


        existingRole.setPermissions(updatedPermissions);


        roleRepository.save(existingRole);
    }


    public List<UserRoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new UserRoleDTO(role.getId(), role.getRoleName()))
                .collect(Collectors.toList());
    }
}
