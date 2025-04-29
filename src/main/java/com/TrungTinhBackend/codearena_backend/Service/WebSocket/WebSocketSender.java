package com.TrungTinhBackend.codearena_backend.Service.WebSocket;

import com.TrungTinhBackend.codearena_backend.DTO.BlogCommentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.LessonCommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBlogComment(BlogCommentDTO blogCommentDTO) {
        messagingTemplate.convertAndSend("/topic/blog" + blogCommentDTO.getBlogId(),blogCommentDTO);
    }

    public void sendLessonComment(LessonCommentDTO lessonCommentDTO) {
        messagingTemplate.convertAndSend("/topic/lesson" + lessonCommentDTO.getLessonId(),lessonCommentDTO);
    }
}
