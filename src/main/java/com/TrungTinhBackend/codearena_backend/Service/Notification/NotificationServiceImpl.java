package com.TrungTinhBackend.codearena_backend.Service.Notification;

import com.TrungTinhBackend.codearena_backend.Entity.Notification;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Repository.*;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestNotification;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private BlogRepository blogRepository;


    @Override
    public APIResponse addNotification(APIRequestNotification apiRequestNotification) {
        APIResponse apiResponse = new APIResponse();

        Notification notification = new Notification();
        notification.setMessage(apiRequestNotification.getMessage());

        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setType(apiRequestNotification.getType());
        notification.setRelatedId(apiRequestNotification.getRelatedId());

        notificationRepository.save(notification);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add notification success !");
        apiResponse.setData(notification);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
