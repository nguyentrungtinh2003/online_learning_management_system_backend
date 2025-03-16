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
        // Tìm token hiện có
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);
        RefreshToken tokenToSave;

        if (optionalRefreshToken.isPresent()) {
            // Nếu token tồn tại -> Cập nhật
            RefreshToken existingToken = optionalRefreshToken.get();
            existingToken.setToken(refreshToken);
            existingToken.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            tokenToSave = existingToken;
        } else {
            // Nếu chưa tồn tại -> Tạo mới
            RefreshToken newToken = new RefreshToken();
            newToken.setToken(refreshToken);
            newToken.setUser(user);
            newToken.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            tokenToSave = newToken;
        }

        // Lưu và trả về token
        return refreshTokenRepository.save(tokenToSave);
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
