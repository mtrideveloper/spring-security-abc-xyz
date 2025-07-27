package com.mtri.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mtri.oauth2.util.PathConstants;

import java.security.Principal; // Cần import này

@Controller
public class UserController {

    @GetMapping(PathConstants.PROFILE_PATH)
    public String profile(Model model, Principal principal) { // Thay đổi thành Principal
        if (principal == null)
            return "redirect:/login";
        /* Không cần trích xuất lại dữ liệu, sử dụng trực tiếp từ GlobalControllerAdvice. Dữ liệu name, email, picture đã được thêm vào model bởi GlobalControllerAdvice
        String name = null;
        String email = null;
        String picture = null;
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            name = (String) attributes.get("name");
            email = (String) attributes.get("email");
            picture = (String) attributes.get("picture");
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            name = email = userDetails.getUsername();
        }
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("picture", picture);*/

        System.out.println("From UserController");
        System.out.println("principal: " + (principal != null ? principal.getClass().getTypeName() : "null"));
        System.out.println("------------------------------");  
        return "profile"; // Trả về file profile.html trong templates
    }
}