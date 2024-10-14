package com.example.demo.auth.DTO;

import java.util.List;

public class RolePermissionDTO {

    private String roleName;           // 接收角色名称
    private List<Integer> permissionIds;  // 接收权限的ID列表

    // 无参构造函数
    public RolePermissionDTO() {
    }

    // 带参构造函数
    public RolePermissionDTO(String roleName, List<Integer> permissionIds) {
        this.roleName = roleName;
        this.permissionIds = permissionIds;
    }

    // Getters and Setters
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}

