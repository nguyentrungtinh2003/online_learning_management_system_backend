package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIRequestNotification {

    private Long receiverId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // Loại thông báo (COURSE, LESSON, QUIZ, BLOG)

    private Long relatedId;

    private String link;
}
