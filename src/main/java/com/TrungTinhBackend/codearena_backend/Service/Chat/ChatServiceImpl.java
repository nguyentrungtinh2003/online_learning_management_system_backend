package com.TrungTinhBackend.codearena_backend.Service.Chat;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRepository;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRoomRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestChat;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public APIResponse addChat(APIRequestChat apiRequestChat, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(apiRequestChat.getSender().getId()).orElseThrow(
                    () -> new NotFoundException("User not found by id " + apiRequestChat.getSender().getId())
            );

            ChatRoom chatRoom = chatRoomRepository.findById(apiRequestChat.getChatRoom().getId()).orElseThrow(
                    () -> new NotFoundException("Chat Room not found by id " + apiRequestChat.getChatRoom().getId())
            );

            Chat chat = new Chat();

            chat.setChatRoom(chatRoom);
            chat.setSender(user);
            if(img != null && !img.isEmpty()) {
                chat.setImg(imgService.uploadImg(img));
            }
            if(video != null && !video.isEmpty()) {
                chat.setVideoURL(videoService.uploadVideo(video));
            }
            chat.setMessage(apiRequestChat.getMessage());
            chat.setDeleted(false);
            chat.setTimestamp(LocalDateTime.now());

            chatRepository.save(chat);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add chat success !");
            apiResponse.setData(chat);
            apiResponse.setTimestamp(LocalDateTime.now());

            messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getId(),chat);

            return apiResponse;
    }

    @Override
    public APIResponse deleteChat(Long id) {
        APIResponse apiResponse = new APIResponse();

            Chat chat = chatRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Chat not found by id " + id)
            );

            chat.setDeleted(true);

            chatRepository.save(chat);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete chat success !");
            apiResponse.setData(chat);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

    }

    @Override
    public APIResponse searchChat(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Chat> chats = chatRepository.searchChat(keyword,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search chat success !");
        apiResponse.setData(chats);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getChatByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Chat> chats = chatRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get chat by page success !");
        apiResponse.setData(chats);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
