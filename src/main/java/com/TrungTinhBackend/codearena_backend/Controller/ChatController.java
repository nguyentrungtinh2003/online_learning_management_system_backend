package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestChat;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Chat.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public ResponseEntity<APIResponse> addChat(@Valid @RequestPart(value = "chat") APIRequestChat apiRequestChat,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        APIResponse apiResponse = chatService.addChat(apiRequestChat, img, video);

        if(apiResponse.getStatusCode() == 200L) {
            Chat saveChat = (Chat) apiResponse.getData();
            String destination = "/topic/chatroom/" + apiRequestChat.getChatRoom().getId();

            messagingTemplate.convertAndSend(destination,saveChat);
        }
        return ResponseEntity.status(apiResponse.getStatusCode().intValue()).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> addChat(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(chatService.deleteChat(id));
    }
}
