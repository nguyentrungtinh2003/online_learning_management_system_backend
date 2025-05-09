package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Entity.RefreshToken;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.RefreshTokenRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.*;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Email.EmailService;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.codearena_backend.Service.RefreshToken.RefreshTokenService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.UserSpecification;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private WebSocketSender webSocketSender;

    public UserServiceImpl(UserRepository userRepository, ImgService imgService, EmailService emailService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, UserSpecification userSpecification) {
        this.userRepository = userRepository;
        this.imgService = imgService;
        this.emailService = emailService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.userSpecification = userSpecification;
    }

    @Override
    public APIResponse login(UserLoginDTO userLoginDTO, HttpServletResponse response) throws Exception {
        APIResponse apiResponse = new APIResponse();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));

            var user = userRepository.findByUsername(userLoginDTO.getUsername());
            if (user == null) {
                throw new NotFoundException("User not found by username " + userLoginDTO.getUsername());
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            refreshTokenService.createRefreshToken(refreshToken,user);

            // Tạo cookie chứa JWT token
            ResponseCookie jwtCookie = ResponseCookie.from("authToken", jwt)
                    .httpOnly(true)
                    .secure(true)// Cookie không thể truy cập từ JavaScript để bảo mật
                    .sameSite("None")
                    .path("/") // Có hiệu lực trên toàn bộ ứng dụng
                    .maxAge(7 * 24 * 60 * 60)
                    .build();// Thêm cookie vào phản hồi

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Login success !");
            apiResponse.setToken(jwt);
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse userRegister(UserRegisterDTO userRegisterDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(userRegisterDTO.getEmail());
            if(user != null) {
                throw new RuntimeException("Email already exists !");
            }
            User user1 = new User();
            user1.setUsername(userRegisterDTO.getUsername());
            user1.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            user1.setEmail(userRegisterDTO.getEmail());
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
    public APIResponse adminRegisterUser(AdminRegisterUserDTO adminRegisterUserDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(adminRegisterUserDTO.getEmail());
            if(user != null) {
                throw new RuntimeException("Email already exists !");
            }
            User user1 = new User();

            if (img != null && !img.isEmpty()) {
                String imgUrl = imgService.uploadImg(img);
                user1.setImg(imgUrl); // Lưu URL của ảnh
            }

            user1.setUsername(adminRegisterUserDTO.getUsername());
            user1.setPassword(passwordEncoder.encode(adminRegisterUserDTO.getPassword()));
            user1.setEmail(adminRegisterUserDTO.getEmail());
            user1.setCoin(0.0);
            user1.setDate(LocalDateTime.now());
            user1.setAddress(adminRegisterUserDTO.getAddress());
            user1.setBirthDay(adminRegisterUserDTO.getBirthDay());
            user1.setPoint(10L);
            user1.setPhoneNumber(adminRegisterUserDTO.getPhoneNumber());
            user1.setRankEnum(RankEnum.BRONZE);
            user1.setRoleEnum(adminRegisterUserDTO.getRoleEnum());
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
    public APIResponse updateUser(Long id, UserUpdateDTO userUpdateDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(id).orElse(null);
            if(user == null) {
                throw new NotFoundException("User not found by id " + id);
            }

            if (img != null && !img.isEmpty()) {
                    String imgUrl = imgService.updateImg(user.getImg(), img);
                    user.setImg(imgUrl);
            }

            if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()) {
                user.setUsername(userUpdateDTO.getUsername());
            }

            if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
            }

            if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isEmpty()) {
                user.setEmail(userUpdateDTO.getEmail());
            }

            if (userUpdateDTO.getCoin() != null) {
                user.setCoin(userUpdateDTO.getCoin());
            }

            if (userUpdateDTO.getAddress() != null && !userUpdateDTO.getAddress().isEmpty()) {
                user.setAddress(userUpdateDTO.getAddress());
            }

            if (userUpdateDTO.getBirthDay() != null) {
                user.setBirthDay(userUpdateDTO.getBirthDay());
            }

            if (userUpdateDTO.getPoint() != null) {
                user.setPoint(userUpdateDTO.getPoint());
            }

            if (userUpdateDTO.getPhoneNumber() != null && !userUpdateDTO.getPhoneNumber().isEmpty()) {
                user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
            }

            if (userUpdateDTO.getRankEnum() != null) {
                user.setRankEnum(userUpdateDTO.getRankEnum());
            }

            if (userUpdateDTO.getRoleEnum() != null) {
                user.setRoleEnum(userUpdateDTO.getRoleEnum());
            }

            if (userUpdateDTO.getStatusUserEnum() != null) {
                user.setStatusUserEnum(userUpdateDTO.getStatusUserEnum());
            }

            if (userUpdateDTO.isEnabled() != user.isEnabled()) {
                user.setEnabled(userUpdateDTO.isEnabled());
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
    public APIResponse restoreUser(Long id) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new NotFoundException("User not found by id " + id);
        }

        user.setEnabled(false);
        user.setDeleted(false);

        userRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore user success");
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
    public APIResponse getUserByEmail(String email) throws Exception {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(email);
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user by email success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getUserByRole(RoleEnum roleEnum) throws Exception {
        APIResponse apiResponse = new APIResponse();

        List<User> users = userRepository.findByRoleEnum(roleEnum);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user by role success");
        apiResponse.setData(users);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getUserByPage(int page, int size) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
            Page<User> page1 = userRepository.findAll(pageable);

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
                newUser.setPoint(0L);
                newUser.setCoin(0.0);
                newUser.setRoleEnum(RoleEnum.STUDENT);
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

        webSocketSender.sendUserInfo(user);

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
    public APIResponse verifyOtpAndChangePassword(UserResetPasswordDTO userResetPasswordDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findByEmail(userResetPasswordDTO.getEmail());
            if(user == null) {
                throw new NotFoundException("User not found by email " + userResetPasswordDTO.getEmail());
            }

            if(!user.getOtp().equals(userResetPasswordDTO.getOtp())) {
                throw new RuntimeException("Invalid OTP !");
            }

            if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);
            }

            user.setOtp(null);
            user.setOtpExpiry(null);
            user.setPassword(passwordEncoder.encode(userResetPasswordDTO.getPassword()));
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
