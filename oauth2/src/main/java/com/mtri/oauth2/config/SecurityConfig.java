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
                .requestMatchers("/", "/home", "/public/**", "/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập không cần đăng nhập
                .requestMatchers("/profile").authenticated() // Yêu cầu đăng nhập cho trang profile
                .anyRequest().authenticated() // Tất cả request khác cần đăng nhập
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/") // Trang chủ sẽ có link đăng nhập
                .defaultSuccessUrl("/profile", true) // Sau khi đăng nhập thành công, chuyển đến profile
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