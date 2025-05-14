package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.DTO.*;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public APIResponse login(UserLoginDTO userLoginDTO,
                             HttpServletResponse response) throws Exception;

    public APIResponse userRegister(UserRegisterDTO userRegisterDTO) throws Exception;

    public APIResponse adminRegisterUser(AdminRegisterUserDTO adminRegisterUserDTO, MultipartFile img) throws Exception;

    public APIResponse updateUser(Long id, UserUpdateDTO userUpdateDTO, MultipartFile img) throws Exception;

    public APIResponse deleteUser(Long id) throws Exception;

    public APIResponse restoreUser(Long id);

    public APIResponse getAllUser() throws Exception;

    public APIResponse countUser();
    public APIResponse getTop5Coin();
    public APIResponse getTop5Point();

    public APIResponse getUserById(Long id) throws Exception;

    public APIResponse getUserByEmail(String email) throws Exception;

    public APIResponse getUserByRole(RoleEnum roleEnum) throws Exception;

    public APIResponse getUserByPage(int page, int size) throws Exception;

    public APIResponse searchUser(String keyword, int page, int size);

    public APIResponse getCurrentUser(Authentication authentication) throws Exception;
    public APIResponse getUserInfo(String jwt);

    public APIResponse logoutGoogle(HttpServletRequest request, HttpServletResponse response);

    public APIResponse sendOtpToEmail(String email) throws Exception;

    public APIResponse verifyOtpAndChangePassword(UserResetPasswordDTO userResetPasswordDTO) throws Exception;

    public APIResponse logout(String username);
}
