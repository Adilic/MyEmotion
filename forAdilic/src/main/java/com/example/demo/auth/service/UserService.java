package com.example.demo.auth.service;

import com.example.demo.auth.DTO.AddUserDTO;
import com.example.demo.auth.DTO.UpdateUserRolesDTO;
import com.example.demo.auth.DTO.UserDTO;
import com.example.demo.auth.DTO.UserRoleDTO;
import com.example.demo.auth.model.Permission;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.User;
import com.example.demo.auth.repository.RoleRepository;
import com.example.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public void updateUserRoles(int userId, UpdateUserRolesDTO updateUserRolesDTO) {

        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));


        Set<Role> updatedRoles = new HashSet<>();
        for (int roleId : updateUserRolesDTO.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            updatedRoles.add(role);
        }


        existingUser.setRoles(updatedRoles);


        userRepository.save(existingUser);
    }

    public void deleteUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void addUser(AddUserDTO addUserDTO) {
        User user = new User();
        user.setUsername(addUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));  // 对密码进行加密


        Set<Role> roles = new HashSet<>();
        for (Integer roleId : addUserDTO.getRoleIds()) {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }
        user.setRoles(roles);


        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<String> getUserPermissions(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {

            System.out.println("User: " + username + " found with roles: " + user.getRoles());


            List<String> permissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())  //
                    .map(Permission::getPermissionName)
                    .distinct()  //
                    .collect(Collectors.toList());


            System.out.println("User: " + username + " Permissions: " + permissions);

            return permissions;
        } else {
            System.out.println("User not found: " + username);
            return new ArrayList<>();
        }
    }


    public List<String> getUserRoles(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {

            List<String> roles = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toList());


            System.out.println("User: " + username + " Roles: " + roles);

            return roles;
        } else {
            System.out.println("User not found: " + username);
            return new ArrayList<>();
        }
    }



}
