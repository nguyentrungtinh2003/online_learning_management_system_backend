package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.LoginLog.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/login-logs")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllLoginLog() {
        return ResponseEntity.ok(loginLogService.getAllLoginLog());
    }
}
