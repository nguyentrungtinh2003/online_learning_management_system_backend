package com.TrungTinhBackend.codearena_backend.Service.Notification;

import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface NotificationService {
    public APIResponse addNotification(NotificationDTO notificationDTO);
    public APIResponse sendSystemNotification(Long userId, String message, String type, Long relatedId);
    public APIResponse getUserNotifications(Long userId);
    public APIResponse searchNotification(String keyword, int page, int size);
    public APIResponse getNotificationByPage(int page, int size);
}
