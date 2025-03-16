package com.TrungTinhBackend.codearena_backend.Service.RefreshToken;

import com.TrungTinhBackend.codearena_backend.Entity.RefreshToken;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày (7 * 24 giờ * 60 phút * 60 giây * 1000 mili giây)

    @Override
    public RefreshToken createRefreshToken(String refreshToken, User user) {

        RefreshToken existingRefreshToken = refreshTokenRepository.findByUser(user).orElseThrow(
                ()-> new NotFoundException("Refresh Token not found")
        );

        if(existingRefreshToken != null) {
            existingRefreshToken.setToken(refreshToken);
            existingRefreshToken.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            refreshTokenRepository.save(existingRefreshToken);
        }

        RefreshToken refreshToken1 = new RefreshToken();
        refreshToken1.setToken(refreshToken);
        refreshToken1.setUser(user);
        refreshToken1.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));

        refreshTokenRepository.save(refreshToken1);
        return refreshToken1;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token invalid !");
        }
        return token;
    }
}
