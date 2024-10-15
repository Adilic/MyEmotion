package com.example.demo.auth.filter;

import com.example.demo.auth.model.CustomUserDetails;
import com.example.demo.auth.model.Permission;
import com.example.demo.auth.model.Role;
import com.example.demo.auth.model.User;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.service.JwtUtil;
import com.example.demo.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// フィルタークラスの宣言
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    // 依存関係の注入
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwtToken);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                CustomUserDetails userDetails = new CustomUserDetails(user);
                // ユーザーの役割リストを取得
                List<String> roles = user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList());

                // 役割ごとに権限リストを取得
                List<String> permissions = user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())  // 各役割の権限を繰り返し処理
                        .map(Permission::getPermissionName)
                        .collect(Collectors.toList());

                // 新しいJWTを生成
                String newJwt = jwtUtil.generateToken(userDetails, roles, permissions);
                response.setHeader("Authorization", "Bearer " + newJwt);

                // 認証情報を設定
                setAuthentication(userDetails, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    // ヘルパーメソッド：認証情報を設定
    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
