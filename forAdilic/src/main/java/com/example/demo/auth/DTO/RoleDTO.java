package com.example.demo.auth.DTO;

import java.util.List;

public class RoleDTO {
    private int id;  // 添加 id 字段
    private String roleName;
    private List<PermissionDTO> permissions;  // 使用 List<PermissionDTO>

    public RoleDTO(int id, String roleName, List<PermissionDTO> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    // 添加 id 的 getter 和 setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
