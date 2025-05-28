package com.TrungTinhBackend.codearena_backend.Service.Chat;

import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
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
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
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
    private WebSocketSender webSocketSender;

    @Autowired
    private NotificationService notificationService;

    public ChatServiceImpl(ImgService imgService, VideoService videoService, ChatRepository chatRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository, WebSocketSender webSocketSender, NotificationService notificationService) {
        this.imgService = imgService;
        this.videoService = videoService;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.webSocketSender = webSocketSender;
        this.notificationService = notificationService;
    }

    @Override
    public APIResponse addChat(ChatDTO chatDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user1 = userRepository.findById(chatDTO.getUser1Id()).orElseThrow(
                    () -> new NotFoundException("User1 not found by id " + chatDTO.getUser1Id())
            );

        User user2 = userRepository.findById(chatDTO.getUser2Id()).orElseThrow(
                () -> new NotFoundException("User2 not found by id " + chatDTO.getUser2Id())
        );

            ChatRoom chatRoom = chatRoomRepository.findById(chatDTO.getChatRoomId()).orElseThrow(
                    () -> new NotFoundException("Chat Room not found by id " + chatDTO.getChatRoomId())
            );

            Chat chat = new Chat();

            chat.setChatRoom(chatRoom);
            chat.setUser1(user1);
            chat.setUser2(user2);
            chat.setChatRoom(chatRoom);
            chat.setMessage(chatDTO.getMessage());
            chat.setTimestamp(LocalDateTime.now());
        chat.setDeleted(false);
//            if(img != null && !img.isEmpty()) {
//                chat.setImg(imgService.uploadImg(img));
//            }
//            if(video != null && !video.isEmpty()) {
//                chat.setVideoURL(videoService.uploadVideo(video));
//            }


            chatRepository.save(chat);

        NotificationDTO notificationDTO = (NotificationDTO) notificationService.sendSystemNotification(user2.getId(), user2.getUsername() +" vừa nhắn tin cho bạn ", "COURSE", 1L).getData();
        webSocketSender.sendNotification(notificationDTO);

            chatDTO.setUser1Img(user1.getImg());
        chatDTO.setUser2Img(user2.getImg());


        apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add chat success !");
            apiResponse.setData(chat);
            apiResponse.setTimestamp(LocalDateTime.now());

            webSocketSender.sendChat(chatDTO);

            return apiResponse;
    }

    @Override
    public APIResponse deleteChat(Long id) {
        APIResponse apiResponse = new APIResponse();

            Chat chat = chatRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Chat not found by id " + id)
            );

//            chat.setDeleted(true);
//
//            chatRepository.save(chat);
        chatRepository.delete(chat);

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
    public APIResponse getChatByChatRoomId(Long chatRoomId) {
        APIResponse apiResponse = new APIResponse();

  List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);

  List<ChatDTO> chatDTOS = chats.stream().map(chat -> {
      ChatDTO chatDTO = new ChatDTO();
      chatDTO.setId(chat.getId());
      chatDTO.setChatRoomId(chat.getChatRoom().getId());
      chatDTO.setMessage(chat.getMessage());
      chatDTO.setUser1Id(chat.getUser1().getId());
      chatDTO.setUser2Id(chat.getUser2().getId());
      chatDTO.setTimeStamp(chat.getTimestamp());
      chatDTO.setUser1Img(chat.getUser1().getImg());
      chatDTO.setUser2Img(chat.getUser2().getImg());
      return chatDTO;
  }).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get chat by chat room id success !");
        apiResponse.setData(chatDTOS);
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
