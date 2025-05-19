package com.TrungTinhBackend.codearena_backend.Service.Jwt;

import com.TrungTinhBackend.codearena_backend.Entity.LoginLog;
import com.TrungTinhBackend.codearena_backend.Service.LoginLog.LoginLogService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final LoginLogService loginLogService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils, LoginLogService loginLogService, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.loginLogService = loginLogService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getAccessTokenFromCookie(request);

        if (jwtToken == null || jwtToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.extractUsername(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<GrantedAuthority> authorities = jwtUtils.extractRoles(jwtToken)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                setAuthentication(userDetails, authorities, request);
            } else {
                String refreshToken = extractRefreshToken(request);
                if (refreshToken != null && jwtUtils.isTokenValid(refreshToken, userDetails)) {
                    String newAccessToken = jwtUtils.generateToken(userDetails);

                    ResponseCookie jwtCookie = ResponseCookie.from("authToken", newAccessToken)
                            .httpOnly(true)
                            .secure(true)
                            .sameSite("None")
                            .path("/")
                            .maxAge(900) // 15 phút
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
                    setAuthentication(userDetails, authorities, request);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        String uri = request.getRequestURI();
        String method = request.getMethod();
        String details = "Accessed endpoint: " + method + " " + uri;

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        if (username != null) {
            boolean success = true;
            // Chỉ log nếu là endpoint quan trọng
            if (!uri.startsWith("/api/ws") && !uri.contains("websocket")) {
                loginLogService.save(new LoginLog(username, ipAddress, success, details));
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails userDetails, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
