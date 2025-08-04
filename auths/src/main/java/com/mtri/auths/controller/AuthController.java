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

    // Trang yêu cầu magic link
    @GetMapping(PathConstants.OTT_REQUEST_PATH)
    public String showOttRequestPage(Model model, Principal principal) {
        if (principal instanceof UserDetails) {
            return "redirect:" + PathConstants.PROFILE_PATH;
        }
        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        return "ott-request";
    }

    // login bằng ott token
    @GetMapping(PathConstants.OTT_LOGIN_PATH)
    public String loginOtt(@RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "error", required = false) String error,
            HttpSession session, Model model, Principal principal) {

        if ("invalid_token".equals(error)) {
            Boolean allow = (Boolean) session.getAttribute("ALLOW_INVALID_TOKEN_ERROR");

            if (Boolean.TRUE.equals(allow)) {
                model.addAttribute("error", "Token không hợp lệ.");
                session.removeAttribute("ALLOW_INVALID_TOKEN_ERROR");
            } else {
                return "redirect:" + PathConstants.OTT_REQUEST_PATH;
            }
        }

        if (principal instanceof UserDetails) {
            System.out.println("You already logged in");
            return "redirect:" + PathConstants.PROFILE_PATH;
        }

        // CHẶN TRUY CẬP TRÁI PHÉP VÀO /ott/login?token=abcxyz
        if (token != null) {
            // Boolean allow = (Boolean) session.getAttribute("ALLOW_OTT_LOGIN");
            // if (!Boolean.TRUE.equals(allow)) {
            // System.out.println("ALLOW_OTT_LOGIN false");
            // return "redirect:" + PathConstants.OTT_REQUEST_PATH;
            // }
            // session.removeAttribute("ALLOW_OTT_LOGIN");
            model.addAttribute("token", token);
        }

        return "ott-login";
    }
}
