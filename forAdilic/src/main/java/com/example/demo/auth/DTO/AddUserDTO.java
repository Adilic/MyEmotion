package com.example.demo.auth.DTO;

import java.util.List;

public class AddUserDTO {

    private String username;
    private String password;
    private List<Integer> roleIds;  // 前端传递的角色 ID 列表

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}