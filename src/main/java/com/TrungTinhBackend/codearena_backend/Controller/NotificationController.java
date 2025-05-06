package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

//    @PostMapping("/add")
//    public ResponseEntity<APIResponse> addNotification(@Valid @RequestBody NotificationDTO notificationDTO) throws Exception {
//        return ResponseEntity.ok(notificationService.addNotification(notificationDTO));
//    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse> getNotificationByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<APIResponse> updateReadNotification(
                                                              @PathVariable Long notificationId) throws Exception {
        return ResponseEntity.ok(notificationService.getUserNotifications(notificationId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchNotification(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.searchNotification(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getNotificationByPage(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(notificationService.getNotificationByPage(page,size));
    }
}
