package com.mtri.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/index", "/public/**", "/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập không cần đăng nhập
                .requestMatchers("/profile").authenticated() // Yêu cầu đăng nhập cho trang profile
                .anyRequest().authenticated() // Tất cả request khác cần đăng nhập
            )
            // ✅ Login form hệ thống
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/profile", true)
                .permitAll()
            )
            // ✅ Login bằng Google OAuth2
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // dùng chung trang login, trang này chứ link login gg
                .defaultSuccessUrl("/profile", true)
            )
            // Spring Boot (và Tomcat embedded) không lưu session vào đĩa giữa các lần chạy
            // JSESSIONID của client (trình duyệt) vẫn còn, nhưng Server không còn biết JSESSIONID đó là ai (vì session bị xóa trong RAM)
            .logout(logout -> logout
                // .logoutUrl("/logout") // Đường dẫn logout
                .logoutSuccessUrl("/") // Sau khi logout, về trang chủ
                // .invalidateHttpSession(true) // Xóa session
                // .clearAuthentication(true) // Xóa thông tin xác thực
                .deleteCookies("JSESSIONID") // Xóa cookies
                .permitAll()
            );
        
        return http.build();
    }
}