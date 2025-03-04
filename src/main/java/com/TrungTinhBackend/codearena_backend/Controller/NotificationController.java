package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestNotification;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addNotification(@Valid @RequestBody APIRequestNotification apiRequestNotification) throws Exception {
        return ResponseEntity.ok(notificationService.addNotification(apiRequestNotification));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse> getNotificationByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchNotification(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(notificationService.searchNotification(keyword, page,size));
    }
}
