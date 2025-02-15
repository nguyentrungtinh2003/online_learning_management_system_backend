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
        String jwtToken = getAccessTokenFromCookie(request);

        if (jwtToken == null || jwtToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.extractUsername(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                setAuthentication(userDetails, request);
            } else {
                // Nếu token hết hạn, thử lấy refresh token để cấp lại
                String refreshToken = extractRefreshToken(request);
                if (refreshToken != null && jwtUtils.isTokenValid(refreshToken, userDetails)) {
                    String newAccessToken = jwtUtils.generateToken(userDetails);

                    // Ghi đè accessToken vào Cookie
                    Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
                    newAccessTokenCookie.setHttpOnly(true);
                    newAccessTokenCookie.setSecure(true);
                    newAccessTokenCookie.setPath("/");
                    newAccessTokenCookie.setMaxAge(900); // 15 phút
                    response.addCookie(newAccessTokenCookie);

                    setAuthentication(userDetails, request);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
