package com.mtri.oauth2.util;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserInfo(Model model, OAuth2AuthenticationToken auth) {
        if (auth != null && auth.isAuthenticated()) {
            Map<String, Object> attributes = auth.getPrincipal().getAttributes();
            String picture = (String) attributes.get("picture");
            String name = (String) attributes.get("name");
            String email = (String) attributes.get("email");
            
            model.addAttribute("picture", picture);
            model.addAttribute("name", name);
            model.addAttribute("emmil", email);
        }
    }
}
