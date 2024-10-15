package com.example.demo.auth.DTO;

import com.example.demo.auth.DTO.UserRoleDTO;

import java.util.List;

public class UserDTO {
    private int id;
    private String username;
    private List<UserRoleDTO> roles;

    public UserDTO(int id, String username, List<UserRoleDTO> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleDTO> roles) {
        this.roles = roles;
    }
}
