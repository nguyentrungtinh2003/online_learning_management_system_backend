package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Entity.RefreshToken;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.RefreshTokenRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.*;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Email.EmailService;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.codearena_backend.Service.RefreshToken.RefreshTokenService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.UserSpecification;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserSpecification userSpecification;

    @Override
    public APIResponse login(APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) throws Exception {
        APIResponse apiResponse = new APIResponse();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(apiRequestUserLogin.getUsername(), apiRequestUserLogin.getPassword()));

            var user = userRepository.findByUsername(apiRequestUserLogin.getUsername());
            if (user == null) {
                throw new NotFoundException("User not found by username " + apiRequestUserLogin.getUsername());
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            refreshTokenService.createRefreshToken(refreshToken,user);

            // Tạo cookie chứa JWT token
            ResponseCookie jwtCookie = ResponseCookie.from("authToken", jwt)
                    .httpOnly(true)
                    .secure(true)// Cookie không thể truy cập từ JavaScript để bảo mật
                    .maxAge(60 * 60)// Cookie hết hạn sau 24 giờ
                    .sameSite("Strict")
                    .path("/") // Có hiệu lực trên toàn bộ ứng dụng
                    .build();// Thêm cookie vào phản hồi

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) // Nếu deploy trên HTTPS
                .path("/")
                .sameSite("Strict")
                .maxAge(60 * 60 * 24 * 30)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Login success !");
            apiResponse.setToken(jwt);
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse userRegister(APIRequestUserRegister apiRequestUserRegister) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(apiRequestUserRegister.getEmail());
            if(user != null) {
                throw new RuntimeException("Email already exists !");
            }
            User user1 = new User();
            user1.setUsername(apiRequestUserRegister.getUsername());
            user1.setPassword(passwordEncoder.encode(apiRequestUserRegister.getPassword()));
            user1.setEmail(apiRequestUserRegister.getEmail());
            user1.setEnabled(true);
            user1.setCoin(0.0);
            user1.setPoint(10L);
            user1.setRoleEnum(RoleEnum.STUDENT);
            user1.setStatusUserEnum(StatusUserEnum.ACTIVE);

            userRepository.save(user1);


            // Gửi email sau khi đăng ký thành công
            String to = user1.getEmail();
            String subject = "Đăng ký thành công";
            String body = "Chào " + user1.getUsername() + ",\n\n"
                    + "Cảm ơn bạn đã đăng ký tài khoản. Chúc bạn trải nghiệm vui vẻ!\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ phát triển";

            emailService.sendEmail(to, subject, body);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User register success");
            apiResponse.setData(user1);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse adminRegisterUser(APIRequestAdminRegisterUser apiRequestAdminRegisterUser, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(apiRequestAdminRegisterUser.getEmail());
            if(user != null) {
                throw new RuntimeException("Email already exists !");
            }
            User user1 = new User();

            if (img != null && !img.isEmpty()) {
                String imgUrl = imgService.uploadImg(img);
                user1.setImg(imgUrl); // Lưu URL của ảnh
            }

            user1.setUsername(apiRequestAdminRegisterUser.getUsername());
            user1.setPassword(passwordEncoder.encode(apiRequestAdminRegisterUser.getPassword()));
            user1.setEmail(apiRequestAdminRegisterUser.getEmail());
            user1.setCoin(0.0);
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
    }

    @Override
    public APIResponse updateUser(Long id,APIRequestUserUpdate apiRequestUserUpdate, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(id).orElse(null);
            if(user == null) {
                throw new NotFoundException("User not found by id " + id);
            }

            if (img != null && !img.isEmpty()) {
                    String imgUrl = imgService.updateImg(user.getImg(), img);
                    user.setImg(imgUrl);
            }

            if (apiRequestUserUpdate.getUsername() != null && !apiRequestUserUpdate.getUsername().isEmpty()) {
                user.setUsername(apiRequestUserUpdate.getUsername());
            }

            if (apiRequestUserUpdate.getPassword() != null && !apiRequestUserUpdate.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(apiRequestUserUpdate.getPassword()));
            }

            if (apiRequestUserUpdate.getEmail() != null && !apiRequestUserUpdate.getEmail().isEmpty()) {
                user.setEmail(apiRequestUserUpdate.getEmail());
            }

            if (apiRequestUserUpdate.getCoin() != null) {
                user.setCoin(apiRequestUserUpdate.getCoin());
            }

            if (apiRequestUserUpdate.getAddress() != null && !apiRequestUserUpdate.getAddress().isEmpty()) {
                user.setAddress(apiRequestUserUpdate.getAddress());
            }

            if (apiRequestUserUpdate.getBirthDay() != null) {
                user.setBirthDay(apiRequestUserUpdate.getBirthDay());
            }

            if (apiRequestUserUpdate.getPoint() != null) {
                user.setPoint(apiRequestUserUpdate.getPoint());
            }

            if (apiRequestUserUpdate.getPhoneNumber() != null && !apiRequestUserUpdate.getPhoneNumber().isEmpty()) {
                user.setPhoneNumber(apiRequestUserUpdate.getPhoneNumber());
            }

            if (apiRequestUserUpdate.getRankEnum() != null) {
                user.setRankEnum(apiRequestUserUpdate.getRankEnum());
            }

            if (apiRequestUserUpdate.getRoleEnum() != null) {
                user.setRoleEnum(apiRequestUserUpdate.getRoleEnum());
            }

            if (apiRequestUserUpdate.getStatusUserEnum() != null) {
                user.setStatusUserEnum(apiRequestUserUpdate.getStatusUserEnum());
            }

            if (apiRequestUserUpdate.isEnabled() != user.isEnabled()) {
                user.setEnabled(apiRequestUserUpdate.isEnabled());
            }

            userRepository.save(user);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User update success");
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse deleteUser(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

           User user = userRepository.findById(id).orElse(null);
           if(user == null) {
               throw new NotFoundException("User not found by id " + id);
           }

           user.setEnabled(false);
           user.setDeleted(true);

           userRepository.save(user);

           apiResponse.setStatusCode(200L);
           apiResponse.setMessage("User delete success");
           apiResponse.setData(user);
           apiResponse.setTimestamp(LocalDateTime.now());
           return apiResponse;
    }

    @Override
    public APIResponse getAllUser() throws Exception {
        APIResponse apiResponse = new APIResponse();

            List<User> users = userRepository.findAllByIsDeleted(false);
            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get all user success");
            apiResponse.setData(users);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse getUserById(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("User not found by id " + id)
            );
            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get user by id "+id+" success");
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse getUserByPage(int page, int size) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Pageable pageable = PageRequest.of(page,size);
            Page<User> page1 = userRepository.findAllByIsDeletedFalse(pageable);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get all user by page "+page+" ,size "+size+" success");
            apiResponse.setData(page1);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse searchUser(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<User> specification = UserSpecification.searchByKeyword(keyword);

        Page<User> users = userRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search user success");
        apiResponse.setData(users);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getCurrentUser(Authentication authentication) throws Exception {
        APIResponse apiResponse = new APIResponse();
            // Lấy thông tin từ Authentication
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oAuth2Token.getPrincipal().getAttributes();

            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String profilePicture = (String) attributes.get("picture");

            // Kiểm tra và lưu vào DB nếu cần
            Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));
            if (existingUser.isEmpty()) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(name);
                newUser.setImg(profilePicture);
                newUser.setProvider("GOOGLE");
                userRepository.save(newUser); // Lưu vào DB
            }

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Login google success");
            apiResponse.setData(Map.of(
                    "email", email,
                    "name", name,
                    "picture", profilePicture
            ));
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse getUserInfo(String jwt) {
        APIResponse apiResponse = new APIResponse();

        String username = jwtUtils.extractUsername(jwt);
        User user = userRepository.findByUsername(username);

        if (jwt == null || !jwtUtils.isTokenValid(jwt,user)) {
            throw  new BadCredentialsException("Token invalid");
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user info success !");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse logoutGoogle(HttpServletRequest request, HttpServletResponse response) {
        APIResponse apiResponse = new APIResponse();
        SecurityContextHolder.clearContext(); // Xóa authentication context
        new SecurityContextLogoutHandler().logout(request, null, null);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Logout success !");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse sendOtpToEmail(String email) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(email);
            if(user == null) {
                throw new NotFoundException("User not found by email " + email);
            }

            String otp = String.format("%06d",new Random().nextInt(999999));
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            emailService.sendEmail(email,"Mã OTP của bạn","OTP : "+otp);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Send OTP success !");
            apiResponse.setData(otp);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse verifyOtpAndChangePassword(APIRequestUserResetPassword apiRequestUserResetPassword) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(apiRequestUserResetPassword.getEmail());
            if(user == null) {
                throw new NotFoundException("User not found by email " + apiRequestUserResetPassword.getEmail());
            }

            if(!user.getOtp().equals(apiRequestUserResetPassword.getOtp())) {
                throw new RuntimeException("Invalid OTP !");
            }

            if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);
            }

            user.setOtp(null);
            user.setOtpExpiry(null);
            user.setPassword(passwordEncoder.encode(apiRequestUserResetPassword.getPassword()));
            userRepository.save(user);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Reset password success !");
            apiResponse.setData(null);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
    }

    @Override
    public APIResponse logout(String username) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByUsername(username);
        Optional<RefreshToken> refreshToken= refreshTokenRepository.findByUser(user);
        refreshToken.ifPresent(refreshTokenRepository::delete);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Logout success !");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
