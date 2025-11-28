package com.mtri.noname.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public static boolean isValidPrincipal(Object principal) {
        return principal != null && (principal instanceof OAuth2AuthenticationToken ||
                principal instanceof OneTimeTokenAuthenticationToken ||
                principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof OAuth2User ||
                principal instanceof UserDetails);

    }
}