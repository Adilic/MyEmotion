package com.example.demo.auth.Controller;

import com.example.demo.auth.DTO.*;
import com.example.demo.auth.model.*;
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

//    @PostMapping("/login")
//    public String createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//        } catch (AuthenticationException e) {
//            throw new Exception("Incorrect username or password", e);
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
//        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
//
//        return jwt;
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // ユーザー名とパスワードを検証
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // ユーザーの詳細情報を取得
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // 役割と権限を取得
            List<String> roles = userService.getUserRoles(userDetails.getUsername());
            List<String> permissions = userService.getUserPermissions(userDetails.getUsername());

            // デバッグ情報を出力
            System.out.println("Roles: " + roles);
            System.out.println("Permissions: " + permissions);

            // JWTを生成
            String token = jwtUtil.generateToken(userDetails, roles, permissions);

            // 生成されたJWTを出力
            System.out.println("Generated JWT: " + token);

            // JWTを返す
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userDetailsService.saveUser(user);
        return "ユーザー登録が完了しました";
    }

    @PreAuthorize("hasAuthority('read')")
    @GetMapping("/get_role_permissions")
    public ResponseEntity<List<RoleDTO>> getRolePermissions() {
        List<RoleDTO> rolesWithPermissions = roleService.getRolesWithPermissions();
        return ResponseEntity.ok(rolesWithPermissions); // 役割と権限のデータを返す
    }

    @PreAuthorize("hasAuthority('read')")
    @GetMapping("/get_permissions")
    public ResponseEntity<List<Permission>> getPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions); // 権限のデータを返す
    }

    @PreAuthorize("hasAuthority('add')")
    @PostMapping("/add_roles")
    public ResponseEntity<String> addRole(@RequestBody RolePermissionDTO rolePermissionDTO) {
        try {
            // Serviceを呼び出して役割と権限を追加
            roleService.addRole(rolePermissionDTO.getRoleName(), rolePermissionDTO.getPermissionIds());
            return ResponseEntity.ok("役割の追加が完了しました");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("役割の追加に失敗しました: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('delete')")
    @DeleteMapping("/delete_roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") int id) {
        boolean isDeleted = roleService.deleteRoleById(id);
        if (isDeleted) {
            return ResponseEntity.ok("役割が正常に削除されました");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("役割が見つかりませんでした、または削除できませんでした");
        }
    }

    @PreAuthorize("hasAuthority('edit')")
    @PutMapping("/update_roles/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") int roleId, @RequestBody UpdateRoleDTO updateRoleDTO) {
        try {
            roleService.updateRole(roleId, updateRoleDTO);
            return ResponseEntity.ok("役割の更新が完了しました");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("役割の更新中にエラーが発生しました: " + e.getMessage());
        }
    }

    // すべてのユーザーとその役割を取得
    @PreAuthorize("hasAuthority('read')")
    @GetMapping("/get_user_roles")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();  // userServiceを使用してユーザー情報を取得
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('read')")
    // すべての役割を取得するエンドポイント
    @GetMapping("/get_roles")
    public ResponseEntity<List<UserRoleDTO>> getAllRoles() {
        List<UserRoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PreAuthorize("hasAuthority('edit')")
    // ユーザーの役割を更新するエンドポイント
    @PutMapping("/update_user_roles/{id}")
    public ResponseEntity<?> updateUserRoles(@PathVariable("id") int userId, @RequestBody UpdateUserRolesDTO updateUserRolesDTO) {
        try {
            userService.updateUserRoles(userId, updateUserRolesDTO);
            return ResponseEntity.ok("ユーザーの役割が正常に更新されました");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ユーザーの役割更新中にエラーが発生しました: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('delete')")
    // ユーザーを削除するエンドポイント
    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("ユーザーが正常に削除されました");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ユーザーの削除中にエラーが発生しました: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('add')")
    // 新しいユーザーを追加するエンドポイント
    @PostMapping("/add_user")
    public ResponseEntity<String> addUser(@RequestBody AddUserDTO addUserDTO) {
        try {
            userService.addUser(addUserDTO);
            return ResponseEntity.ok("ユーザーが正常に追加されました");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ユーザー追加中にエラーが発生しました: " + e.getMessage());
        }
    }
}
