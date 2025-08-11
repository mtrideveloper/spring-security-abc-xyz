package com.mtri.auths.service.auth.oauth2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import com.mtri.auths.model.User;
import com.mtri.auths.repo.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1) load attributes from provider (Google)
        OAuth2User oauthUser = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauthUser.getAttributes();

        // 2) get email (depends on provider; Google returns "email")
        String email = (String) attributes.get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("OAuth2 provider did not return email");
        }

        // 3) find or create local DB user
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setName((String) attributes.getOrDefault("name", email));
            // u.setAvatar((String) attributes.getOrDefault("picture", "@{/images/noise.jpg}"));
            u.setAvatar((String) attributes.get("picture"));
            u.setRoles(List.of("ROLE_CLIENT")); // default role for new OAuth users
            return userRepository.save(u);
        });

        // 4) build authorities from DB roles (ensure ROLE_ prefix)
        List<SimpleGrantedAuthority> authorities = (user.getRoles() == null) ? List.of() :
            user.getRoles().stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 5) nameAttributeKey: lấy từ ClientRegistration (đảm bảo tương thích đa provider)
        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 6) trả về DefaultOAuth2User với authorities từ DB
        return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
    }
}
