package com.mtri.oauth2.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Tôi muốn tự tạo và quản lý một đối tượng (bean) này trong ApplicationContext của Spring
 * @Configuration Giúp Spring biết đây là nơi để tìm và đăng ký bean vào hệ thống.
 * Đây là cách cấu hình Java-based thay cho applicationContext.xml truyền thống.
 * @Bean Mỗi phương thức đánh dấu `@Bean` sẽ được gọi và kết quả trả về sẽ trở thành 1 bean trong Spring context.
 */
@Configuration
public class UserTestingConfig {
    @Bean
    public UserDetailsService userDetails() {
        // InMemoryUserDetailsManager là một lớp có sẵn trong Spring Security dùng để quản lý người dùng 
        // trong bộ nhớ RAM (in-memory). Đây là cách kiểm tra authentication (xác thực) 
        // mà không cần kết nối cơ sở dữ liệu
        return new InMemoryUserDetailsManager(
                User.withUsername("unitydev2d@gmail.com")
                        .password("{noop}password")
                        .roles("USER")
                        .build());
    }
}
