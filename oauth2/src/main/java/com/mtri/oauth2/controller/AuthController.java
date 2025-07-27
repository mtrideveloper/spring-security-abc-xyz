package com.mtri.oauth2.controller;

import java.security.Principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mtri.oauth2.util.PathConstants;

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
            System.out.println("AUTHOR OAUTH2: " +principal.getName());
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
            return "redirect:/profile";
        }
        return "ott-sent";
    }

    // OTT Login Page (GET: Hiển thị form với token)
    @GetMapping(PathConstants.OTT_LOGIN_PATH)
    public String ottLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "token", required = false) String token,
            Model model, Principal principal) {
        if (error != null) {
            model.addAttribute("error", "Đăng nhập thất bại. Vui lòng kiểm tra email hoặc thử lại.");
        }
        if (token != null) {
            model.addAttribute("token", token);
        }
        if (principal != null && principal instanceof UserDetails) {
            System.out.println("principal: " + principal.getName());
            return "redirect:/profile";
        }
        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        return "ott-login";
    }    
}
