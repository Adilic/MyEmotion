package com.example.demo.auth.DTO;

public class UserRoleDTO {
    private int id;
    private String roleName;

    public UserRoleDTO(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    // Getters and Setters
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
}
