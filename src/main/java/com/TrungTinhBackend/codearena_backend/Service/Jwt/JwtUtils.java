package com.TrungTinhBackend.codearena_backend.Service.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final SecretKey key;

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 phút
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày

    public JwtUtils() {
        String secretString = "563858594676085648355464835539438557565846474655";
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);  // Tạo SecretKey chuẩn HS256
    }

    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst() // Lấy quyền đầu tiên (vì chỉ có 1 role)
                .map(GrantedAuthority::getAuthority) // Lấy tên quyền (VD: "ROLE_ADMIN")
                .orElse("ROLE_USER"); // Giá trị mặc định nếu không có quyền
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",role)  // Thêm claim role
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst() // Lấy quyền đầu tiên (vì chỉ có 1 role)
                .map(GrantedAuthority::getAuthority) // Lấy tên quyền (VD: "ROLE_ADMIN")
                .orElse("ROLE_USER"); // Giá trị mặc định nếu không có quyền
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("role", role)  // Thêm claim role
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public List<GrantedAuthority> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            List<String> roles = claims.get("role", List.class);
            if (roles == null) return Collections.emptyList();
            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Lỗi khi lấy role từ JWT: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
