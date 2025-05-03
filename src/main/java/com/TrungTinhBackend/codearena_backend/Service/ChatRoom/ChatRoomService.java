package com.TrungTinhBackend.codearena_backend.Service.ChatRoom;

import com.TrungTinhBackend.codearena_backend.DTO.ChatRoomDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface ChatRoomService {
    public APIResponse addChatRoom(ChatRoomDTO chatRoomDTO);
}
