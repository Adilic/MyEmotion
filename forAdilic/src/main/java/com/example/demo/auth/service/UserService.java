package com.example.demo.auth.service;

import com.example.demo.auth.DTO.AddUserDTO;
import com.example.demo.auth.DTO.UpdateUserRolesDTO;
import com.example.demo.auth.DTO.UserDTO;
import com.example.demo.auth.DTO.UserRoleDTO;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.User;
import com.example.demo.auth.repository.RoleRepository;
import com.example.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 获取所有用户及其角色
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles().stream()
                                .map(role -> new UserRoleDTO(role.getId(), role.getRoleName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 更新用户角色的逻辑
    public void updateUserRoles(int userId, UpdateUserRolesDTO updateUserRolesDTO) {
        // 获取用户
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 获取新的角色集合
        Set<Role> updatedRoles = new HashSet<>();
        for (int roleId : updateUserRolesDTO.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            updatedRoles.add(role);
        }

        // 更新用户角色
        existingUser.setRoles(updatedRoles);

        // 保存更新后的用户
        userRepository.save(existingUser);
    }
    // 删除用户的逻辑
    public void deleteUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);  // 删除用户
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void addUser(AddUserDTO addUserDTO) {
        User user = new User();
        user.setUsername(addUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));  // 对密码进行加密

        // 获取用户选择的角色
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : addUserDTO.getRoleIds()) {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }
        user.setRoles(roles);

        // 保存新用户
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
