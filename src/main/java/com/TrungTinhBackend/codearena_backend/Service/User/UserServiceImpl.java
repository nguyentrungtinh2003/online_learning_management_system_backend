package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestAdminRegisterUser;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

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

    @Override
    public APIResponse userRegister(APIRequestUserRegister apiRequestUserRegister) {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findByEmail(apiRequestUserRegister.getEmail());
            if(user != null) {
                apiResponse.setStatusCode(500L);
                apiResponse.setMessage("Email already exists");
                apiResponse.setTimestamp(LocalDateTime.now());
                return apiResponse;
            }
            User user1 = new User();
            user1.setUsername(apiRequestUserRegister.getUsername());
            user1.setPassword(passwordEncoder.encode(apiRequestUserRegister.getPassword()));
            user1.setEmail(apiRequestUserRegister.getEmail());

            userRepository.save(user1);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User register success");
            apiResponse.setData(user1);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;

        } catch (BadCredentialsException e) {
            apiResponse.setStatusCode(403L);
            apiResponse.setMessage("Invalid credentials");
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage(e.getMessage());
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }
    }

    @Override
    public APIResponse adminRegisterUser(APIRequestAdminRegisterUser apiRequestAdminRegisterUser, MultipartFile img) {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findByEmail(apiRequestAdminRegisterUser.getEmail());
            if(user != null) {
                apiResponse.setStatusCode(500L);
                apiResponse.setMessage("Email already exists");
                apiResponse.setTimestamp(LocalDateTime.now());
                return apiResponse;
            }
            User user1 = new User();

            if (img != null && !img.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());
                String imgUrl = uploadResult.get("url").toString();
                user1.setImg(imgUrl); // Lưu URL của ảnh
            }

            user1.setUsername(apiRequestAdminRegisterUser.getUsername());
            user1.setPassword(passwordEncoder.encode(apiRequestAdminRegisterUser.getPassword()));
            user1.setEmail(apiRequestAdminRegisterUser.getEmail());
            user1.setCoin(0L);
            user1.setDate(LocalDateTime.now());
            user1.setAddress(apiRequestAdminRegisterUser.getAddress());
            user1.setBirthDay(apiRequestAdminRegisterUser.getBirthDay());
            user1.setPoint(10L);
            user1.setPhoneNumber(apiRequestAdminRegisterUser.getPhoneNumber());
            user1.setRankEnum(RankEnum.BRONZE);
            user1.setRoleEnum(apiRequestAdminRegisterUser.getRoleEnum());
            user1.setStatusUserEnum(StatusUserEnum.ACTIVE);
            user1.setEnabled(true);

            userRepository.save(user1);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User register success");
            apiResponse.setData(user1);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;

        } catch (BadCredentialsException e) {
            apiResponse.setStatusCode(403L);
            apiResponse.setMessage("Invalid credentials");
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage(e.getMessage());
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }
    }
}
