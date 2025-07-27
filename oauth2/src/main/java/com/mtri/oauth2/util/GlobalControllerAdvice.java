package com.mtri.oauth2.util;

import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        // add paths
        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        model.addAttribute("ottLoginUrl", PathConstants.OTT_LOGIN_PATH);
        model.addAttribute("sprOttLoginApi", PathConstants.SPR_OTT_LOGIN_API);
        model.addAttribute("sprOttGenApi", PathConstants.SPR_OTT_GEN_API);
        
        if (principal != null) {
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
            } else if (principal instanceof OneTimeTokenAuthenticationToken) {
                OneTimeTokenAuthenticationToken ottToken = (OneTimeTokenAuthenticationToken) principal;
                // Lấy thông tin từ OneTimeTokenAuthenticationToken
                Object principalDetails = ottToken.getPrincipal();
                if (principalDetails instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) principalDetails;
                    Map<String, Object> attributes = oauth2User.getAttributes();
                    name = (String) attributes.get("name");
                    email = (String) attributes.get("email");
                    picture = (String) attributes.get("picture");
                } else if (principalDetails instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principalDetails;
                    name = email = userDetails.getUsername();
                }
            }
            // else {
            // name = email = principal.getName();
            // }

            System.out.println("From GlobalControllerAdvice");
            System.out.println("principal: " + principal.getClass().getTypeName());
            System.out.println("name: " + name);
            System.out.println("email: " + email);
            System.out.println("pic: " + picture);
            System.out.println("--------------------------------");

            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("picture", picture);
        }
    }
}
