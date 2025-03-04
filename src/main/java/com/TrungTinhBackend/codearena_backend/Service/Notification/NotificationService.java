package com.TrungTinhBackend.codearena_backend.Service.Notification;

import com.TrungTinhBackend.codearena_backend.Entity.Notification;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestNotification;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

import java.util.List;

public interface NotificationService {
    public APIResponse addNotification(APIRequestNotification apiRequestNotification);
    public APIResponse sendSystemNotification(Long userId, String message, String type, Long relatedId);
    public APIResponse getUserNotifications(Long userId);
    public APIResponse searchNotification(String keyword, int page, int size);
}
