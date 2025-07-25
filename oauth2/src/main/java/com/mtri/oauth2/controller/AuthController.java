package com.mtri.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Nếu đã đăng nhập thì redirect về trang chủ
            return "redirect:/";
        }
        return "public/login"; // trả về login.html nếu chưa đăng nhập
    }
}
