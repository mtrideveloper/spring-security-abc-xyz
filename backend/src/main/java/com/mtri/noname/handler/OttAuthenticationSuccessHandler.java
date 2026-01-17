package com.mtri.noname.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mtri.noname.service.auth.UserService;
import com.mtri.noname.util.PathConstants;

@Component
public class OttAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;

    public OttAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        String username = authentication.getName();

        // Tạo mới nếu chưa tồn tại
        userService.createUserIfNotExists(username, "OTT");

        // #region Unnecessary (or not ?)
        //// Load lại user (đảm bảo có roles) từ UserDetails của spring security
        // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
        //         userDetails.getPassword(), userDetails.getAuthorities());

        //// Lưu SecurityContext hiện tại vào HttpSession (để dùng cho các request sau) ?
        // SecurityContextHolder.getContext().setAuthentication(auth);
        // #endregion

        // // Giả sử bạn có hàm tạo JWT
        // String token = "eyJhbGciOiJIUzI1NiIsIn...";

        // // 3. Ghi phản hồi JSON
        // response.getWriter().write("{\"token\": \"" + token + "\", \"message\":
        // \"Login Success\"}");

        // // Lưu ý: Không gọi response.sendRedirect() ở đây nếu làm API
        // redirect profile page
        response.sendRedirect(PathConstants.PROFILE_PATH);
    }
}
