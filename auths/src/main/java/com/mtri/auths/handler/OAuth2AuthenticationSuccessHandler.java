package com.mtri.auths.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Lấy thông tin user từ OAuth2
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getName(); // Google ID
        
        System.out.println("OAuth2 Login Success:");
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
        System.out.println("Provider ID: " + providerId);
        
        // TODO: Lưu thông tin user vào database nếu chưa tồn tại
        // userService.createOrUpdateUser(email, name, providerId, "GOOGLE");
        
        // TODO: Tạo JWT token nếu cần
        // String jwtToken = jwtService.generateToken(email);
        // response.addHeader("Authorization", "Bearer " + jwtToken);
        
        // Redirect về trang profile
        response.sendRedirect("/profile");
    }
}