package com.mtri.devicechecker.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        // add paths
        model.addAttribute("loginUrl", PathConstants.LOGIN_PATH);
        model.addAttribute("ottLoginUrl", PathConstants.OTT_LOGIN_PATH);
        model.addAttribute("ottRequestUrl", PathConstants.OTT_REQUEST_PATH);
        model.addAttribute("sprOttLoginApi", PathConstants.SPR_OTT_LOGIN_API);
        model.addAttribute("sprOttGenApi", PathConstants.SPR_OTT_GEN_API);
        model.addAttribute("recaptchaV2Gg", PathConstants.RECAPTCHAV2GG);

        if (principal != null) {
            String name = null;
            String email = null;
            String picture = null;

            if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
                OAuth2User oauth2User = oauth2Token.getPrincipal();
                var attr = oauth2User.getAttributes();
                name = (String) attr.get("name");
                email = (String) attr.get("email");
                picture = (String) attr.get("picture");
            } else if (principal instanceof UsernamePasswordAuthenticationToken upToken) {
                Object inner = upToken.getPrincipal();
                if (inner instanceof UserDetails userDetails) {
                    name = userDetails.getUsername();
                    email = userDetails.getUsername();
                } else if (inner instanceof OneTimeTokenAuthenticationToken ottToken) {
                    name = email = ottToken.getName();
                }
            } else if (principal instanceof OneTimeTokenAuthenticationToken ottToken) {
                name = email = ottToken.getName();
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
