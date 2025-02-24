package com.TrungTinhBackend.codearena_backend.Entity;

import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = true)
    private User receiver;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // Loại thông báo (COURSE, LESSON, QUIZ, BLOG)

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;// Trạng thái (UNREAD, READ)

    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    private Long relatedId;

    private String link;
}
