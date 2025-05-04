package com.TrungTinhBackend.codearena_backend.Service.WebSocket;

import com.TrungTinhBackend.codearena_backend.DTO.BlogCommentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.ChatDTO;
import com.TrungTinhBackend.codearena_backend.DTO.LessonCommentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBlogComment(BlogCommentDTO blogCommentDTO) {
        messagingTemplate.convertAndSend("/topic/blog/" + blogCommentDTO.getBlogId(),blogCommentDTO);
    }

    public void sendLessonComment(LessonCommentDTO lessonCommentDTO) {
        messagingTemplate.convertAndSend("/topic/lesson/" + lessonCommentDTO.getLessonId(),lessonCommentDTO);
    }

    public void sendChat(ChatDTO chatDTO) {
        messagingTemplate.convertAndSend("/topic/chat-room/" + chatDTO.getChatRoomId(),chatDTO);
    }

    public void sendLike(Long blogId, Long userId, List<Long> likedUserIds) {
        messagingTemplate.convertAndSend("/topic/like/" + blogId,likedUserIds);
    }

    public void sendUnLike(Long blogId, Long userId, List<Long> likedUserIds) {
        messagingTemplate.convertAndSend("/topic/un-like/" + blogId,likedUserIds);
    }

    public void sendNotification(NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSend("/topic/user/" + notificationDTO.getReceiverId(),notificationDTO);
    }
}
