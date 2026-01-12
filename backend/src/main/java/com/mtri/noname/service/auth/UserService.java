package com.mtri.noname.service.auth;

import org.springframework.stereotype.Service;

import com.mtri.noname.model.User;
import com.mtri.noname.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUserIfNotExists(String email, String loginMethod) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(extractNameFromEmail(email));
                    newUser.setRoles(List.of("ROLE_USER")); // role mặc định
                    newUser.setLoginMethod(loginMethod);
                    return userRepository.save(newUser);
                });
    }

    private String extractNameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        return (atIndex > 0) ? email.substring(0, atIndex) : email;
    }
}

