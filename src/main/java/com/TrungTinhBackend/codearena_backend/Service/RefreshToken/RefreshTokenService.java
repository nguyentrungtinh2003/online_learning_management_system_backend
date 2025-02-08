package com.TrungTinhBackend.codearena_backend.Service.RefreshToken;

import com.TrungTinhBackend.codearena_backend.Entity.RefreshToken;
import com.TrungTinhBackend.codearena_backend.Entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    // Tạo refresh token mới
    public RefreshToken createRefreshToken(String refreshToken, User user);
    // Tìm refresh token trong database
    public Optional<RefreshToken> findByToken(String token);

    // Kiểm tra refresh token có hết hạn chưa
    public RefreshToken verifyExpiration(RefreshToken token);

    // Xóa token khi user logout
    public void deleteByUser(User user);
}
