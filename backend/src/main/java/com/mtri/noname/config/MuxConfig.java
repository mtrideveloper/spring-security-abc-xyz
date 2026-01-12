package com.mtri.noname.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mux.ApiClient;
import com.mux.auth.HttpBasicAuth;


import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MuxConfig {
    @Value("${mux.token-id}")
    private String tokenId;

    @Value("${mux.token-secret}")
    private String tokenSecret;

    @Bean
    public ApiClient muxApiClient() {
        // Khởi tạo Client mặc định
        ApiClient client = com.mux.Configuration.getDefaultApiClient();

        // Cấu hình xác thực (Username = TokenID, Password = SecretKey)
        HttpBasicAuth accessToken = (HttpBasicAuth) client.getAuthentication("accessToken");
        accessToken.setUsername(tokenId);
        accessToken.setPassword(tokenSecret);

        return client;
    }
}
