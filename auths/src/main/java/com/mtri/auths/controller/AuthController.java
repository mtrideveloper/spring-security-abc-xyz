package com.mtri.auths.controller;

import java.security.Principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mtri.auths.util.PathConstants;
import com.mtri.auths.util.SecurityChecker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        if (SecurityChecker.isValidPrincipal(principal)) {
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
    public String showOttRequestPage(
            Model model, Principal principal,
            @RequestParam(value = "error", required = false) String error,
            HttpSession session) {
        // B3: kiểm tra param error có phải invalid_token
        if ("invalid_token".equals(error)) {
            CheckErrorWithSessionFlag(model, session);
        }
        System.out.println("Error: " + model.getAttribute("error"));

        if (SecurityChecker.isValidPrincipal(principal)) {
            return "redirect:" + PathConstants.PROFILE_PATH;
        }
        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        return "ott-request";
    }

    // login bằng ott token
    @GetMapping(PathConstants.OTT_LOGIN_PATH)
    public String loginOtt(
            @RequestParam(value = "token", required = false) String token,
            Model model, Principal principal,
            HttpSession session, HttpServletResponse response, HttpServletRequest request) {

        if (principal instanceof UserDetails) {
            return "redirect:" + PathConstants.PROFILE_PATH;
        }

        // Kiểm tra token - nếu không có token thì redirect về trang request
        if (token == null || token.trim().isEmpty()) {
            session.setAttribute("ALLOW_INVALID_TOKEN_ERROR", true);
            return "redirect:" + PathConstants.OTT_REQUEST_PATH + "?error=invalid_token";
        }

        if (token != null) {
            model.addAttribute("token", token);
        }

        return "ott-login";
    }

    private void CheckErrorWithSessionFlag(Model model, HttpSession session) {
        // B4: lấy session flag ALLOW_INVALID_TOKEN_ERROR đã lưu tại B1
        Boolean allow = (Boolean) session.getAttribute("ALLOW_INVALID_TOKEN_ERROR");
        if (Boolean.TRUE.equals(allow)) // tránh null
        {
            System.out.println("ott token invalid");
            // B5: lưu vào attr để dùng trong thymeleaf
            model.addAttribute("error", "Token không hợp lệ.");
        }
        // B6: xóa flag để lần refresh sau không hiển thị lại thông báo lỗi
        session.removeAttribute("ALLOW_INVALID_TOKEN_ERROR");
    }
}
