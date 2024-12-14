package com.TrungTinhBackend.codearena_backend.Service.User;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    public APIResponse login(APIRequestUserLogin apiRequestUserLogin,
                             HttpServletResponse response);
}
