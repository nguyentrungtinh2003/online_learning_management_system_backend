package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Entity.RefreshToken;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestRefreshToken;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.codearena_backend.Service.RefreshToken.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/token")
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/refresh")
    public APIResponse refreshToken(@Valid @RequestBody APIRequestRefreshToken apiRequestRefreshToken) {
        String refreshToken = apiRequestRefreshToken.getToken();

        RefreshToken refreshToken1 = refreshTokenService.findByToken(apiRequestRefreshToken.getToken()).orElseThrow(
                () -> new NotFoundException("Refresh token not found !")
        );

        refreshTokenService.verifyExpiration(refreshToken1);

        String accessToken = jwtUtils.generateToken(refreshToken1.getUser());

        APIResponse apiResponse = new APIResponse();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Refresh token success !");
        apiResponse.setToken(accessToken);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
