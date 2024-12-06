package com.TrungTinhBackend.codearena_backend.Service.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        // Kiểm tra xem header Authorization có chứa token không
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy JWT từ Authorization header
        jwtToken = authHeader.substring(7); // "Bearer " bỏ đi
        username = jwtUtils.extractUsername(jwtToken);

        // Nếu token hợp lệ, set Authentication vào SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            } else {
                // Nếu token hết hạn, dùng refresh token để cấp lại access token
                String refreshToken = extractRefreshToken(request);
                if (refreshToken != null && jwtUtils.isTokenValid(refreshToken, userDetails)) {
                    // Tạo access token mới từ refresh token
                    String newAccessToken = jwtUtils.generateToken(userDetails);

                    // Cập nhật lại cookie hoặc header với access token mới
                    response.setHeader("Authorization", "Bearer " + newAccessToken);  // Hoặc thêm vào cookie

                    // Cập nhật SecurityContext với access token mới
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    newToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(newToken);
                    SecurityContextHolder.setContext(securityContext);
                } else {
                    // Nếu refresh token không hợp lệ, yêu cầu người dùng đăng nhập lại
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // Lấy refresh token từ header hoặc cookie (tùy thuộc vào cách bạn lưu trữ)
    private String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = null;

        // Lấy refresh token từ header
        String refreshHeader = request.getHeader("Refresh-Token");
        if (refreshHeader != null) {
            refreshToken = refreshHeader;
        }
        // Hoặc lấy từ cookies (được lưu dưới dạng HttpOnly cookie)
        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}
