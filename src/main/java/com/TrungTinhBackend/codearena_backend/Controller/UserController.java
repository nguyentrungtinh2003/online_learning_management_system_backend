package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestAdminRegisterUser;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserLogin;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserUpdate;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user-register")
    public ResponseEntity<APIResponse> userRegister(@Valid @RequestBody APIRequestUserRegister apiRequestUserRegister) throws Exception {
        return ResponseEntity.ok(userService.userRegister(apiRequestUserRegister));
    }

    @PostMapping("/admin-register-user")
    public ResponseEntity<APIResponse> adminRegisterUser(@Valid @RequestPart("user") APIRequestAdminRegisterUser apiRequestAdminRegisterUser,
                                                         @RequestPart("img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.adminRegisterUser(apiRequestAdminRegisterUser,img));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.login(apiRequestUserLogin,response));
    }
    //--google ----
    @GetMapping("/user")
    public ResponseEntity<APIResponse> getCurrentUser(Authentication authentication) throws Exception {
        return ResponseEntity.ok(userService.getCurrentUser(authentication));
    }

    @GetMapping("/all-user")
    public ResponseEntity<APIResponse> getAllUser() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/user-page")
    public ResponseEntity<APIResponse> getUserByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(userService.getUserByPage(page,size));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/logout/google")
    public ResponseEntity<APIResponse> logoutGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.logoutGoogle(request,response));
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id,@Valid @RequestPart APIRequestUserUpdate apiRequestUserUpdate,@RequestPart MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.updateUser(id,apiRequestUserUpdate,img));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
