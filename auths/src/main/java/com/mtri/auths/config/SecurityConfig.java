package com.mtri.auths.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.mtri.auths.handler.MagicLinkGenerationSuccessHandler;
import com.mtri.auths.handler.OAuth2AuthenticationSuccessHandler;
import com.mtri.auths.handler.OttAuthenticationFailureHandler;
import com.mtri.auths.handler.OttAuthenticationSuccessHandler;
import com.mtri.auths.service.auth.oauth2.CustomOAuth2UserService;
import com.mtri.auths.util.PathConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MagicLinkGenerationSuccessHandler magicLinkHandler;
    private final OttAuthenticationSuccessHandler ottSuccessHandler;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(MagicLinkGenerationSuccessHandler magicLinkHandler, OttAuthenticationSuccessHandler ottSuccessHandler, CustomOAuth2UserService oAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler) {
        this.magicLinkHandler = magicLinkHandler;
        this.oAuth2UserService = oAuth2UserService;
        this.ottSuccessHandler = ottSuccessHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index", "/public/**", "/unauth/**",
                                PathConstants.LOGIN_PATH,
                                PathConstants.OTT_LOGIN_PATH,
                                PathConstants.OTT_SENT_PATH,
                                PathConstants.RECAPTCHAV2GG,
                                PathConstants.RECAPTCHAV2GGFORM,
                                "/css/**", "/js/**", "/images/**")
                        .permitAll() // Cho phép truy cập không cần đăng nhập
                        .requestMatchers("/profile").authenticated() // Yêu cầu đăng nhập cho trang profile
                        .anyRequest().authenticated() // Tất cả request khác cần đăng nhập
                )
                .oneTimeTokenLogin(ott -> ott
                        .loginPage(PathConstants.OTT_LOGIN_PATH)
                        .defaultSuccessUrl(PathConstants.PROFILE_PATH, true)
                        .successHandler(ottSuccessHandler)
                        // .failureUrl(PathConstants.OTT_LOGIN_PATH + "?error=invalid_token")
                        .failureHandler(new OttAuthenticationFailureHandler())
                        .tokenGenerationSuccessHandler(magicLinkHandler))
                // ✅ Login bằng Google OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(PathConstants.LOGIN_PATH)
                        .defaultSuccessUrl(PathConstants.PROFILE_PATH, true)
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .failureUrl(PathConstants.LOGIN_PATH + "?error"))
                // Spring Boot (và Tomcat embedded) không lưu session vào đĩa giữa các lần chạy
                // JSESSIONID của client (trình duyệt) vẫn còn, nhưng Server không còn biết
                // JSESSIONID đó là ai (vì session bị xóa trong RAM)
                .logout(logout -> logout
                        // .logoutUrl("/logout") // Đường dẫn logout
                        // .invalidateHttpSession(true) // Xóa session
                        // .clearAuthentication(true) // Xóa thông tin xác thực
                        .logoutSuccessUrl("/") // Sau khi logout, về trang chủ
                        .logoutUrl(PathConstants.LOGOUT_PATH)
                        // .logoutSuccessUrl(PathConstants.LOGIN_PATH + "?logout")
                        .deleteCookies("JSESSIONID")); // Xóa cookies
                        // .permitAll());

        return http.build();
    }

}