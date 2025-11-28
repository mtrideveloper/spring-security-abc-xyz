package com.mtri.noname.service.auth.ott;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.mtri.noname.model.User;
import com.mtri.noname.service.auth.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService; // thêm UserService

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    // Load by username = email
    // @Override // ghi đè pthuc loadUserByUsername của Spring Security
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByEmail(username)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found: " +
    // username));

    // List<GrantedAuthority> authorities = toAuthorities(user.getRoles());
    // // password not used for OTT, but set empty to satisfy contract
    // return org.springframework.security.core.userdetails.User
    // .withUsername(user.getEmail())
    // .password("{noop}password")
    // .authorities(authorities)
    // .accountExpired(false)
    // .accountLocked(false)
    // .credentialsExpired(false)
    // .disabled(false)
    // .build();
    // }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // tạo user mới nếu chưa tồn tại
        User user = userService.createUserIfNotExists(username, "gg");

        List<GrantedAuthority> authorities = toAuthorities(user.getRoles());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("{noop}password") // OTT không dùng password
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private List<GrantedAuthority> toAuthorities(List<String> roles) {
        // user có thể đăng nhập nhưng không có quyền gì, dễ gây lỗi khi truy cập trang
        // yêu cầu role.
        if (roles == null || roles.isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return roles.stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
