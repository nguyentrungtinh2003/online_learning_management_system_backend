package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestAdminRegisterUser;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserUpdate;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Email.EmailService;
import com.TrungTinhBackend.codearena_backend.Service.Jwt.JwtUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public APIResponse login(APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(apiRequestUserLogin.getUsername(), apiRequestUserLogin.getPassword()));

            var user = userRepository.findByUsername(apiRequestUserLogin.getUsername());
            if (user == null) {
                throw new RuntimeException("User not found !");
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
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse userRegister(APIRequestUserRegister apiRequestUserRegister) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findByEmail(apiRequestUserRegister.getEmail());
            if(user != null) {
                throw new RuntimeException("Email already exists !");
            }
            User user1 = new User();
            user1.setUsername(apiRequestUserRegister.getUsername());
            user1.setPassword(passwordEncoder.encode(apiRequestUserRegister.getPassword()));
            user1.setEmail(apiRequestUserRegister.getEmail());
            user1.setEnabled(true);
            user1.setCoin(0L);
            user1.setPoint(10L);
            user1.setRoleEnum(RoleEnum.STUDENT);

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

        } catch (BadCredentialsException e) {
           throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse adminRegisterUser(APIRequestAdminRegisterUser apiRequestAdminRegisterUser, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findByEmail(apiRequestAdminRegisterUser.getEmail());
            if(user != null) {
                throw new RuntimeException("User not found !");
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
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateUser(Long id,APIRequestUserUpdate apiRequestUserUpdate, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findById(id).orElse(null);
            if(user == null) {
                throw new RuntimeException("User not found !");
            }

            if (img != null && !img.isEmpty()) {
                try {
                    if(user.getImg() != null && !user.getImg().isEmpty()) {
                        String oldImgUrl = user.getImg();
                        String publicID = oldImgUrl.substring(oldImgUrl.lastIndexOf("/") + 1,oldImgUrl.lastIndexOf("."));
                        cloudinary.uploader().destroy(publicID,ObjectUtils.emptyMap());
                    }
                    Map uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());
                    String imgUrl = uploadResult.get("url").toString();
                    user.setImg(imgUrl); // Lưu URL của ảnh
                }catch (Exception e) {
                    throw new Exception("Message : "+e.getMessage(),e);
                }
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

            if(apiRequestUserUpdate.getDate() != null) {
                user.setDate(apiRequestUserUpdate.getDate());
            }

            userRepository.save(user);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User update success");
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse deleteUser(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
           User user = userRepository.findById(id).orElse(null);
           if(user == null) {
               throw new RuntimeException("User not found !");
           }

           user.setEnabled(false);
           user.setDeleted(true);

           userRepository.save(user);

           apiResponse.setStatusCode(200L);
           apiResponse.setMessage("User delete success");
           apiResponse.setData(user);
           apiResponse.setTimestamp(LocalDateTime.now());
           return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse getAllUser() throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            List<User> users = userRepository.findAllByIsDeleted(false);
            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get all user success");
            apiResponse.setData(users);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse getUserById(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findById(id).orElse(null);
            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get user by id "+id+" success");
            apiResponse.setData(user);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse getUserByPage(int page, int size) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<User> page1 = userRepository.findAllByIsDeletedFalse(pageable);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Get all user by page "+page+" ,size "+size+" success");
            apiResponse.setData(page1);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse getCurrentUser(Authentication authentication) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
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
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
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

}
