package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public APIResponse login(APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) {
        APIResponse apiResponse = new APIResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(apiRequestUserLogin.getUsername(), apiRequestUserLogin.getPassword()));

            var user = userRepository.findByUsername(apiRequestUserLogin.getUsername());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            // Tạo cookie chứa JWT token
            Cookie jwtCookie = new Cookie("authToken", jwt);
            jwtCookie.setHttpOnly(true); // Cookie không thể truy cập từ JavaScript để bảo mật
            jwtCookie.setMaxAge(24 * 60 * 60); // Cookie hết hạn sau 24 giờ
            jwtCookie.setPath("/"); // Có hiệu lực trên toàn bộ ứng dụng
            response.addCookie(jwtCookie); // Thêm cookie vào phản hồi

            // Thêm refresh token vào cookie
            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true); // Nếu đang sử dụng HTTPS
            refreshTokenCookie.setPath("/"); // Có thể truy cập toàn bộ website
            refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // Đặt thời gian sống cho cookie (ví dụ: 30 ngày)
            response.addCookie(refreshTokenCookie);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Login success !");
            apiResponse.setToken(jwt);
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            apiResponse.setStatusCode(403L);
            apiResponse.setMessage("Invalid credentials");
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage(e.getMessage());
            return apiResponse;
        }
    }
}
