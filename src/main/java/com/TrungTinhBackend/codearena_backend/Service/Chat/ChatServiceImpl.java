package com.TrungTinhBackend.codearena_backend.Service.Chat;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRepository;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRoomRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.ChatDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

    public ChatServiceImpl(ImgService imgService, VideoService videoService, ChatRepository chatRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository, SimpMessagingTemplate messagingTemplate) {
        this.imgService = imgService;
        this.videoService = videoService;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public APIResponse addChat(ChatDTO chatDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(chatDTO.getSenderId()).orElseThrow(
                    () -> new NotFoundException("User not found by id " + chatDTO.getSenderId())
            );

            ChatRoom chatRoom = chatRoomRepository.findById(chatDTO.getChatRoom().getId()).orElseThrow(
                    () -> new NotFoundException("Chat Room not found by id " + chatDTO.getChatRoom().getId())
            );

            Chat chat = new Chat();

            chat.setChatRoom(chatRoom);
            chat.setSender(user);
//            if(img != null && !img.isEmpty()) {
//                chat.setImg(imgService.uploadImg(img));
//            }
//            if(video != null && !video.isEmpty()) {
//                chat.setVideoURL(videoService.uploadVideo(video));
//            }
            chat.setMessage(chatDTO.getMessage());
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

    @Override
    public APIResponse getAllChat() {
        APIResponse apiResponse = new APIResponse();

        List<Chat> chats = chatRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all chat success !");
        apiResponse.setData(chats);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getChatById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Chat chat = chatRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Chat not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get chat by id success !");
        apiResponse.setData(chat);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
