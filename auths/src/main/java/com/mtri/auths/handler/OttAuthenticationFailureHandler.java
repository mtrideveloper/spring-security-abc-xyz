package com.mtri.auths.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.mtri.auths.util.PathConstants;

import java.io.IOException;

@Component
public class OttAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // B1: Đặt session flag ALLOW_INVALID_TOKEN_ERROR vào RAM server hiển thị lỗi token không hợp lệ
        request.getSession().setAttribute("ALLOW_INVALID_TOKEN_ERROR", true);

        // B2: Redirect tới /ott-request?error=invalid_token
        response.sendRedirect(PathConstants.OTT_REQUEST_PATH + "?error=invalid_token");

        
    }
}
