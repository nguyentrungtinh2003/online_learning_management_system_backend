package com.TrungTinhBackend.codearena_backend.Service.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {
    private SecretKey Key;

    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 phút
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày (7 * 24 giờ * 60 phút * 60 giây * 1000 mili giây)

    public JwtUtils() {
        String secretString = "563858594676085648355464835539438557565846474655";
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");

    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities())  // Thêm claim cho role
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities())  // Thêm claim cho role
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
