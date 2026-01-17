package com.mtri.noname.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;

public class SecurityChecker {

    /**
     * Checks if the provided principal is an instance of UserDetails,
     * UsernamePasswordAuthenticationToken, or OneTimeTokenAuthenticationToken.
     *
     * @param principal the object to check
     * @return true if the principal is one of the specified types, false otherwise
     */
    public static boolean isValidPrincipal(Principal principal) {
        return principal != null && (principal instanceof OAuth2AuthenticationToken ||
                principal instanceof OneTimeTokenAuthenticationToken ||
                principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof OAuth2User ||
                principal instanceof UserDetails);

    }

    public static String extractEmailFromPrincipal(Principal principal) {
        String email = null;

        if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            var attr = oauth2User.getAttributes();
            email = (String) attr.get("email");
        } else if (principal instanceof UsernamePasswordAuthenticationToken upToken) {
            Object inner = upToken.getPrincipal();
            if (inner instanceof UserDetails userDetails) {
                email = userDetails.getUsername();
            } else if (inner instanceof OneTimeTokenAuthenticationToken ottToken) {
                email = ottToken.getName();
            }
        } else if (principal instanceof OneTimeTokenAuthenticationToken ottToken) {
            email = ottToken.getName();
        }

        return email;
    }
}