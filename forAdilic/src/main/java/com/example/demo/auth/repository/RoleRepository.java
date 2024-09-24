package com.example.demo.auth.repository;

import com.example.demo.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // 可以根据需要添加自定义查询方法
}