package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.ChatDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Chat.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public ResponseEntity<APIResponse> addChat(@Valid @RequestPart(value = "chat") ChatDTO chatDTO,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        APIResponse apiResponse = chatService.addChat(chatDTO, img, video);

        return ResponseEntity.status(apiResponse.getStatusCode().intValue()).body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllChat() {
        return ResponseEntity.ok(chatService.getAllChat());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchChat(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(chatService.searchChat(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getChatByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(chatService.getChatByPage(page,size));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteChat(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(chatService.deleteChat(id));
    }
}
