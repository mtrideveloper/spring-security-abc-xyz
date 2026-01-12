package com.mtri.noname.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.mtri.noname.handler.MagicLinkGenerationSuccessHandler;
import com.mtri.noname.handler.OAuth2AuthenticationSuccessHandler;
import com.mtri.noname.handler.OttAuthenticationFailureHandler;
import com.mtri.noname.handler.OttAuthenticationSuccessHandler;
import com.mtri.noname.util.PathConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MagicLinkGenerationSuccessHandler magicLinkHandler;
    private final OttAuthenticationSuccessHandler ottSuccessHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(MagicLinkGenerationSuccessHandler magicLinkHandler, OttAuthenticationSuccessHandler ottSuccessHandler, OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler) {
        this.magicLinkHandler = magicLinkHandler;
        this.ottSuccessHandler = ottSuccessHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tắt CSRF để cho phép các yêu cầu POST từ React
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index", "/public/**", "/unauth/**",
                                PathConstants.LOGIN_PATH,
                                PathConstants.OTT_REQUEST_PATH,
                                PathConstants.OTT_LOGIN_PATH,
                                PathConstants.OTT_SENT_PATH,
                                PathConstants.RECAPTCHAV2GG,
                                PathConstants.RECAPTCHAV2GGFORM,
                                "/css/**", "/js/**", "/images/**",
                                "/studio/**") // temporary
                        .permitAll() // Cho phép truy cập không cần đăng nhập
                        .anyRequest().authenticated() // Tất cả request khác cần đăng nhập
                )
                .oneTimeTokenLogin(ott -> ott
                        // .loginPage(PathConstants.OTT_LOGIN_PATH)
                        .loginPage(PathConstants.OTT_REQUEST_PATH)  // sao để cái path này cũng được ??
                        // .loginProcessingUrl(PathConstants.OTT_LOGIN_PATH) // đặt đây thì nó ghi đè luôn api spring xử lý token là /login/ott
                        // .defaultSuccessUrl(PathConstants.PROFILE_PATH, true)
                        .successHandler(ottSuccessHandler)
                        // .failureUrl(PathConstants.OTT_LOGIN_PATH + "?error=invalid_token")
                        .failureHandler(new OttAuthenticationFailureHandler())
                        .tokenGenerationSuccessHandler(magicLinkHandler))
                // ✅ Login bằng Google OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/dcm-ao-thiet-day")
                        // .loginPage(PathConstants.LOGIN_PATH)
                        // .defaultSuccessUrl(PathConstants.PROFILE_PATH, true)
                        .successHandler(oAuth2SuccessHandler)
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