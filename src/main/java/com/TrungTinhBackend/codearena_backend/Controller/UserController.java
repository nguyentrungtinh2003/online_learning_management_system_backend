package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.*;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
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

    @PostMapping("register")
    public ResponseEntity<APIResponse> userRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        return ResponseEntity.ok(userService.userRegister(userRegisterDTO));
    }

    @PostMapping("admin/register/user")
    public ResponseEntity<APIResponse> adminRegisterUser(@Valid @RequestPart(value = "user") AdminRegisterUserDTO adminRegisterUserDTO,
                                                         @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.adminRegisterUser(adminRegisterUserDTO,img));
    }

    @PostMapping("login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.login(userLoginDTO,response));
    }
    //--google ----
    @GetMapping("user-google")
    public ResponseEntity<APIResponse> getCurrentUser(Authentication authentication) throws Exception {
        return ResponseEntity.ok(userService.getCurrentUser(authentication));
    }
    //--info user login ---
    @GetMapping("user-info")
    public ResponseEntity<APIResponse> getUserInfo(@CookieValue(value = "authToken", required = false) String jwt) throws Exception {
        return ResponseEntity.ok(userService.getUserInfo(jwt));
    }

    @GetMapping("/user/all")
    public ResponseEntity<APIResponse> getAllUser() throws Exception {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("admin/user/page")
    public ResponseEntity<APIResponse> getUserByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(userService.getUserByPage(page,size));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("user/email/{email}")
    public ResponseEntity<APIResponse> getUserByEmail(@PathVariable String email) throws Exception {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("user/role/{roleEnum}")
    public ResponseEntity<APIResponse> getUserByRole(@PathVariable RoleEnum roleEnum) throws Exception {
        return ResponseEntity.ok(userService.getUserByRole(roleEnum));
    }

    @GetMapping("admin/user/search")
    public ResponseEntity<APIResponse> searchUser(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(userService.searchUser(keyword, page, size));
    }

    @GetMapping("logout/google")
    public ResponseEntity<APIResponse> logoutGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.logoutGoogle(request,response));
    }

    @PutMapping("user/update/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id, @Valid @RequestPart(value = "user") UserUpdateDTO userUpdateDTO, @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(userService.updateUser(id,userUpdateDTO,img));
    }

    @DeleteMapping("admin/user/delete/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @DeleteMapping("admin/user/restore/{id}")
    public ResponseEntity<APIResponse> restoreUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.restoreUser(id));
    }

    @DeleteMapping("logout")
    public ResponseEntity<APIResponse> logout(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.logout(username));
    }

    // API gửi OTP đến email
    @PostMapping("forgot-password")
    public ResponseEntity<APIResponse> forgotPassword(@RequestBody Map<String, String> payload) throws Exception {
        String email = payload.get("email");
        return ResponseEntity.ok(userService.sendOtpToEmail(email));
    }

    // API xác nhận OTP và thay đổi mật khẩu
    @PostMapping("reset-password")
    public ResponseEntity<APIResponse> resetPassword(@RequestBody UserResetPasswordDTO userResetPasswordDTO) throws Exception {
        return ResponseEntity.ok(userService.verifyOtpAndChangePassword(userResetPasswordDTO));
    }
}
