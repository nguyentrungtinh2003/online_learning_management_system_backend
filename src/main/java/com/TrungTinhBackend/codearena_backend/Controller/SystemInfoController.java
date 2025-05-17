package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.SystemInfoDTO;
import com.TrungTinhBackend.codearena_backend.Entity.SystemInfo;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.AuditLog.AuditLogService;
import com.TrungTinhBackend.codearena_backend.Service.SystemInfo.SystemInfoService;
import com.TrungTinhBackend.codearena_backend.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api")
public class SystemInfoController {

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/admin/system-info/add")
    public ResponseEntity<APIResponse> addSystemInfo(@RequestPart("system") SystemInfoDTO systemInfoDTO,
                                                     @RequestPart("img") MultipartFile img) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"ADD","Add system info");
        return ResponseEntity.ok(systemInfoService.addSystemInfo(systemInfoDTO,img));
    }

    @GetMapping("/admin/system-info/{id}")
    public ResponseEntity<APIResponse> getSystemInfo(@PathVariable Long id) throws Exception {
//        String username = SecurityUtils.getCurrentUsername();
//        auditLogService.addLog(username,"VIEW","View system info");
        return ResponseEntity.ok(systemInfoService.getSystemInfo(id));
    }

    @PutMapping("/admin/update/system-info/{id}")
    public ResponseEntity<APIResponse> updateSystemInfo(@PathVariable Long id,
                                                        @RequestPart("system") SystemInfoDTO systemInfoDTO,
                                                        @RequestPart("img") MultipartFile img) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"UPDATE","Update system info");
        return ResponseEntity.ok(systemInfoService.updateSystemInfo(id,systemInfoDTO,img));
    }

    @DeleteMapping("/admin/delete/system-info/{id}")
    public ResponseEntity<APIResponse> deleteSystemInfo(@PathVariable Long id) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"DELETE","Delete system info");
        return ResponseEntity.ok(systemInfoService.deleteSystemInfo(id));
    }
}
