package com.example.demo.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
    @Table(name = "user")
    @Data
    @NoArgsConstructor
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false, unique = true, length = 50)
        private String username;

        @Column(nullable = false, length = 100)
        private String password;

        @Column(unique = true, length = 100)
        private String email;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role",  // 中间表名称
                joinColumns = @JoinColumn(name = "user_id"),  // 当前类在中间表的外键
                inverseJoinColumns = @JoinColumn(name = "role_id")  // 关联类在中间表的外键
        )
        private Set<Role> roles = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
