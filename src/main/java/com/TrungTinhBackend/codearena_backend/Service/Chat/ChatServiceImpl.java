package com.TrungTinhBackend.codearena_backend.Service.Chat;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRepository;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRoomRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestChat;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public APIResponse addChat(APIRequestChat apiRequestChat, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            User user = userRepository.findById(apiRequestChat.getSender().getId()).orElseThrow(
                    () -> new RuntimeException("User not found !")
            );

            ChatRoom chatRoom = chatRoomRepository.findById(apiRequestChat.getChatRoom().getId()).orElseThrow(
                    () -> new RuntimeException("Chat Room not found !")
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

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }
}
