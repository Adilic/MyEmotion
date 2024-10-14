package com.example.demo.auth.DTO;

import java.util.List;

public class UpdateRoleDTO {

    private String roleName;
    private List<Integer> permissionIds;  // 用于存放权限的ID列表

    public UpdateRoleDTO() {
    }

    public UpdateRoleDTO(String roleName, List<Integer> permissionIds) {
        this.roleName = roleName;
        this.permissionIds = permissionIds;
    }

    // Getter 和 Setter 方法
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
