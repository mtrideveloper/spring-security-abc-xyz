package com.mtri.auths.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mtri.auths.service.auth.UserService;
import com.mtri.auths.service.auth.ott.CustomUserDetailsService;

@Component
public class OttAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    public OttAuthenticationSuccessHandler(CustomUserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        String username = authentication.getName();

        // Tạo mới nếu chưa tồn tại
        userService.createUserIfNotExists(username, "OTT");

        // Load lại user (đảm bảo có roles) từ UserDetails của spring security
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        super.onAuthenticationSuccess(request, response, auth);
    }
}

