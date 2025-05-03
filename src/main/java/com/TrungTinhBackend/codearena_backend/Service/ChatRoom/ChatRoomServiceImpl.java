package com.TrungTinhBackend.codearena_backend.Service.ChatRoom;

import com.TrungTinhBackend.codearena_backend.DTO.ChatRoomDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRoomRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public APIResponse addChatRoom(ChatRoomDTO chatRoomDTO) {
        APIResponse apiResponse = new APIResponse();

        User user1 = userRepository.findById(chatRoomDTO.getUser1Id()).orElseThrow(
                () -> new NotFoundException("User1 not found !")
        );

        User user2 = userRepository.findById(chatRoomDTO.getUser2Id()).orElseThrow(
                () -> new NotFoundException("User2 not found !")
        );

        ChatRoom chatRoom1 = chatRoomRepository.findChatRoomByUserIds(chatRoomDTO.getUser1Id(), chatRoomDTO.getUser2Id());

        if(chatRoom1 == null) {

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setUser1(user1);
            chatRoom.setUser2(user2);
            chatRoom.setDeleted(false);

            chatRoomRepository.save(chatRoom);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add chat room success !");
            apiResponse.setData(chatRoom);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
        }
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Chat room success !");
        apiResponse.setData(chatRoom1);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
