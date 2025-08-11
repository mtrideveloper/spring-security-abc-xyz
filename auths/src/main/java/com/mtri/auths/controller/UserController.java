package com.mtri.auths.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mtri.auths.util.PathConstants;

import java.security.Principal; // Cần import này

@Controller
public class UserController {

    @GetMapping(PathConstants.PROFILE_PATH)
    public String profile(Model model, Principal principal) { // Thay đổi thành Principal
        if (principal == null)
            return "redirect:/login"; // Trả về HTTP 302 Redirect. Trình duyệt gửi một request mới, có thay đổi URL trên browser.
                                    // 	Server chuyển tiếp nội bộ request đến một controller khác, không thay đổi URL trên browser.

        System.out.println("From UserController");
        System.out.println("principal: " + (principal != null ? principal.getClass().getTypeName() : "null"));
        System.out.println("------------------------------");  
        return "profile"; // Trả về file profile.html trong templates
    }
}