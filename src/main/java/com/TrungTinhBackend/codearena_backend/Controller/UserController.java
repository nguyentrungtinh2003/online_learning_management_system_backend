package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestAdminRegisterUser;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user-register")
    public ResponseEntity<APIResponse> userRegister(@Valid @RequestBody APIRequestUserRegister apiRequestUserRegister) {
        return ResponseEntity.ok(userService.userRegister(apiRequestUserRegister));
    }

    @PostMapping("/admin-register-user")
    public ResponseEntity<APIResponse> adminRegisterUser(@Valid @RequestPart("user") APIRequestAdminRegisterUser apiRequestAdminRegisterUser,
                                                         @RequestPart("img") MultipartFile img) {
        return ResponseEntity.ok(userService.adminRegisterUser(apiRequestAdminRegisterUser,img));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(apiRequestUserLogin,response));
    }
}
