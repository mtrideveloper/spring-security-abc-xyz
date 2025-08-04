package com.mtri.auths.controller;

import java.security.Principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mtri.auths.util.PathConstants;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    // OAuth2 Login Page
    @GetMapping(PathConstants.LOGIN_PATH)
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model, Principal principal) {

        if (error != null) {
            model.addAttribute("error", "Đăng nhập thất bại. Vui lòng thử lại.");
        }
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công.");
        }

        if (principal != null) {
            System.out.println("AUTHOR OAUTH2: " + principal.getName());
            return "redirect:/profile";
        }

        model.addAttribute("ottLoginUrl", PathConstants.OTT_LOGIN_PATH);
        return "login";
    }

    // Page hiển thị sau khi gửi magic link
    @GetMapping(PathConstants.OTT_SENT_PATH)
    public String ottSent(Model model, Principal principal) {
        model.addAttribute("message", "Magic link đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.");
        if (principal != null) {
            System.out.println("principal: " + principal.getName());
            return "redirect:" + PathConstants.PROFILE_PATH;
        }
        return "ott-sent";
    }

    // OTT Login Page (GET: Hiển thị form với token)
    @GetMapping(PathConstants.OTT_LOGIN_PATH)
    public String ottLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "token", required = false) String token,
            Model model,
            Principal principal,
            HttpSession session) {

        // Kiểm tra nếu có lỗi và lỗi là invalid_token
        if ("invalid_token".equals(error)) {
            Boolean allowError = (Boolean) session.getAttribute("ALLOW_INVALID_TOKEN_ERROR");

            if (Boolean.TRUE.equals(allowError)) {
                model.addAttribute("error", "Token không hợp lệ.");
                session.removeAttribute("ALLOW_INVALID_TOKEN_ERROR"); // Xóa flag sau khi dùng
            } else {
                // Người dùng gõ tay URL, không cho hiển thị lỗi
                return "redirect:" + PathConstants.OTT_LOGIN_PATH;
            }
        }

        // Nếu có token (khi người dùng nhấp link trong email), truyền vào form
        if (token != null && !token.isBlank()) {
            model.addAttribute("token", token);
        }

        // Nếu đã đăng nhập, chuyển về profile
        if (principal instanceof UserDetails) {
            return "redirect:" + PathConstants.PROFILE_PATH;
        }

        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        return "ott-login";
    }
}
