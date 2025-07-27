package com.mtri.oauth2.util;

public class PathConstants {
    // OAuth2 paths
    public static final String LOGIN_PATH = "/login";
    // OTT paths
    public static final String OTT_LOGIN_PATH = "/ott/login";
    public static final String OTT_SENT_PATH = "/ott-sent";
    
    // Common paths
    public static final String PROFILE_PATH = "/profile";
    public static final String HOME_PATH = "/";
    public static final String LOGOUT_PATH = "/logout";

    // Spring Security APIs, controller không override các api này!
    public static final String SPR_OTT_LOGIN_API = "/login/ott";
    public static final String SPR_OTT_GEN_API = "/ott/generate";
    
    // errors 
    public static final String TOKEN_INVALID = "error/token-invalid";

    private PathConstants(){}
}