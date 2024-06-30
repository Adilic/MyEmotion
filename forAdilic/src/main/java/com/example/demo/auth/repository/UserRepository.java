package com.example.demo.auth.repository;

import com.example.demo.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 可以添加一些自定义查询方法，例如通过用户名查找用户
    User findByUsername(String username);
}