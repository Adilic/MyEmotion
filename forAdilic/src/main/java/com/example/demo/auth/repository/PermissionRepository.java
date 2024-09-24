package com.example.demo.auth.repository;

import com.example.demo.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    // 可以根据需要添加自定义查询方法
}