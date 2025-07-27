package com.mtri.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppUser {
    private String name;
    private String email;
    private String picture;
}
