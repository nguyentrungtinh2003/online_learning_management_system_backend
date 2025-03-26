package com.TrungTinhBackend.codearena_backend.Service.Notification;

import com.TrungTinhBackend.codearena_backend.Entity.Notification;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.*;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestNotification;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, CourseRepository courseRepository, LessonRepository lessonRepository, QuizRepository quizRepository, SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.quizRepository = quizRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public APIResponse addNotification(APIRequestNotification apiRequestNotification) {
        APIResponse apiResponse = new APIResponse();
        Notification notification = new Notification();
        List<Long> allUserIds = userRepository.getAllUserIds();  // Lấy tất cả user ID

        for (Long userId : allUserIds) {
            notification.setMessage(apiRequestNotification.getMessage());
            notification.setReceiver(userRepository.findById(apiRequestNotification.getReceiver().getId()).orElseThrow(
                    () -> new NotFoundException("User not found !")
            ));
            notification.setCreatedAt(LocalDateTime.now());
            notification.setStatus(NotificationStatus.UNREAD);
            notification.setType(apiRequestNotification.getType());
            notification.setRelatedId(apiRequestNotification.getRelatedId()); // Clone object
            notificationRepository.save(notification);

            // Gửi WebSocket cho từng user
            messagingTemplate.convertAndSend("/topic/user/" + userId, notification);
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add notification success !");
        apiResponse.setData(notification);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse sendSystemNotification(Long userId, String message,String type,Long relatedId) {
        APIResponse apiResponse = new APIResponse();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setReceiver(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        ));
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setType(NotificationType.valueOf(type));
        notification.setRelatedId(relatedId);

        notificationRepository.save(notification);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add notification success !");
        apiResponse.setData(notification);
        apiResponse.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/user/"+userId,notification);
        return apiResponse;
    }

    @Override
    public APIResponse getUserNotifications(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Notification> notifications = notificationRepository.findByReceiver_IdOrderByCreatedAtDesc(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get notification by user success !");
        apiResponse.setData(notifications);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse searchNotification(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Notification> notifications = notificationRepository.searchNotification(keyword,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search notification success !");
        apiResponse.setData(notifications);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getNotificationByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Notification> notifications = notificationRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get notification by page success !");
        apiResponse.setData(notifications);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
