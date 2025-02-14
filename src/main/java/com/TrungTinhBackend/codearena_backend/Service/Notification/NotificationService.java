package com.TrungTinhBackend.codearena_backend.Service.Notification;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestNotification;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface NotificationService {
    public APIResponse addNotification(APIRequestNotification apiRequestNotification);
}
