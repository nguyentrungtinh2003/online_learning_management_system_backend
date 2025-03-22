package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.*;
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

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user-register")
    public ResponseEntity<APIResponse> userRegister(@Valid @RequestBody APIRequestUserRegister apiRequestUserRegister) throws Exception {
        return ResponseEntity.ok(userService.userRegister(apiRequestUserRegister));
    }

    @PostMapping("/admin/admin-register-user")
    public ResponseEntity<APIResponse> adminRegisterUser(@Valid @RequestPart(value = "user") APIRequestAdminRegisterUser apiRequestAdminRegisterUser,
                                                         @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.adminRegisterUser(apiRequestAdminRegisterUser,img));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody APIRequestUserLogin apiRequestUserLogin, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.login(apiRequestUserLogin,response));
    }
    //--google ----
    @GetMapping("/user-google")
    public ResponseEntity<APIResponse> getCurrentUser(Authentication authentication) throws Exception {
        return ResponseEntity.ok(userService.getCurrentUser(authentication));
    }
    //--info user login ---
    @GetMapping("/user-info")
    public ResponseEntity<APIResponse> getUserInfo(@CookieValue(value = "authToken", required = false) String jwt) throws Exception {
        return ResponseEntity.ok(userService.getUserInfo(jwt));
    }

    @GetMapping("/admin/all-user")
    public ResponseEntity<APIResponse> getAllUser() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/admin/page")
    public ResponseEntity<APIResponse> getUserByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(userService.getUserByPage(page,size));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchUser(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(userService.searchUser(keyword, page, size));
    }

    @GetMapping("/logout/google")
    public ResponseEntity<APIResponse> logoutGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.logoutGoogle(request,response));
    }

    @PutMapping("/admin/update-user/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id,@Valid @RequestPart(value = "user") APIRequestUserUpdate apiRequestUserUpdate,@RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.updateUser(id,apiRequestUserUpdate,img));
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<APIResponse> logout(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.logout(username));
    }

    // API gửi OTP đến email
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse> forgotPassword(@RequestBody Map<String, String> payload) throws Exception {
        String email = payload.get("email");
        return ResponseEntity.ok(userService.sendOtpToEmail(email));
    }

    // API xác nhận OTP và thay đổi mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse> resetPassword(@RequestBody APIRequestUserResetPassword apiRequestUserResetPassword) throws Exception {
        return ResponseEntity.ok(userService.verifyOtpAndChangePassword(apiRequestUserResetPassword));
    }
}
