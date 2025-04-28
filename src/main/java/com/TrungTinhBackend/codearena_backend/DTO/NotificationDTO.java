package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long receiverId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // Loại thông báo (COURSE, LESSON, QUIZ, BLOG)

    private Long relatedId;

    private String link;
}
