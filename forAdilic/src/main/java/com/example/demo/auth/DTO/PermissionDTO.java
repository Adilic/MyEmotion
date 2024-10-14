package com.example.demo.auth.DTO;

public class PermissionDTO {
    private int id;
    private String permissionName;

    public PermissionDTO(int id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
