package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestAdminRegisterUser;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserUpdate;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public APIResponse login(APIRequestUserLogin apiRequestUserLogin,
                             HttpServletResponse response) throws Exception;

    public APIResponse userRegister(APIRequestUserRegister apiRequestUserRegister) throws Exception;

    public APIResponse adminRegisterUser(APIRequestAdminRegisterUser apiRequestAdminRegisterUser, MultipartFile img) throws Exception;

    public APIResponse updateUser(Long id,APIRequestUserUpdate apiRequestUserUpdate, MultipartFile img) throws Exception;

    public APIResponse deleteUser(Long id) throws Exception;
}
