package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestChat;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Chat.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addChat(@Valid @RequestPart(value = "chat") APIRequestChat apiRequestChat,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(chatService.addChat(apiRequestChat, img, video));
    }
}
