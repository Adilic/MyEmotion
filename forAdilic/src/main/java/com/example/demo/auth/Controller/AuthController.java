package com.example.demo.auth.Controller;

import com.example.demo.auth.DTO.*;
import com.example.demo.auth.model.LoginRequest;
import com.example.demo.auth.model.Permission;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.User;
import com.example.demo.auth.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return jwt;
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userDetailsService.saveUser(user);
        return "User registered successfully";
    }

    @GetMapping("/get_role_permissions")
//    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<RoleDTO>> getRolePermissions() {
        List<RoleDTO> rolesWithPermissions = roleService.getRolesWithPermissions();
        return ResponseEntity.ok(rolesWithPermissions); // 返回角色和权限数据
    }

    @GetMapping("/get_permissions")
//    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<Permission>> getPermissions() {
        List<Permission> Permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(Permissions); // 返回角色和权限数据
    }

    @PostMapping("/add_roles")
//    @PreAuthorize("hasAuthority('ADD')")
    public ResponseEntity<String> addRole(@RequestBody RolePermissionDTO rolePermissionDTO) {
        try {
            // 调用 Service 添加角色和权限
            roleService.addRole(rolePermissionDTO.getRoleName(), rolePermissionDTO.getPermissionIds());
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add role: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete_roles/{id}")
//    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deleteRole(@PathVariable("id") int id) {
        boolean isDeleted = roleService.deleteRoleById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Role deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found or could not be deleted");
        }
    }

    @PutMapping("/update_roles/{id}")
//    @PreAuthorize("hasAuthority('EDIT')")
    public ResponseEntity<?> updateRole(@PathVariable("id") int roleId, @RequestBody UpdateRoleDTO updateRoleDTO) {
        try {
            roleService.updateRole(roleId, updateRoleDTO);
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating role: " + e.getMessage());
        }
    }

    // 获取所有用户及其角色
    @GetMapping("/get_user_roles")
//    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();  // 使用 userService 获取用户信息
        return ResponseEntity.ok(users);
    }

    // 获取所有角色的端点
    @GetMapping("/get_roles")
//    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<UserRoleDTO>> getAllRoles() {
        List<UserRoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    // 更新用户角色的端点
    @PutMapping("/update_user_roles/{id}")
//    @PreAuthorize("hasAuthority('EDIT')")
    public ResponseEntity<?> updateUserRoles(@PathVariable("id") int userId, @RequestBody UpdateUserRolesDTO updateUserRolesDTO) {
        try {
            userService.updateUserRoles(userId, updateUserRolesDTO);
            return ResponseEntity.ok("User roles updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user roles: " + e.getMessage());
        }
    }
    // 删除用户的端点
    @DeleteMapping("/delete_user/{id}")
//    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
    // 添加新用户
    @PostMapping("/add_user")
//    @PreAuthorize("hasAuthority('ADD')")
    public ResponseEntity<String> addUser(@RequestBody AddUserDTO addUserDTO) {
        try {
            userService.addUser(addUserDTO);
            return ResponseEntity.ok("User added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding user: " + e.getMessage());
        }
    }
}



