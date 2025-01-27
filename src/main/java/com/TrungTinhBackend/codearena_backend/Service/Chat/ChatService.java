package com.TrungTinhBackend.codearena_backend.Service.Chat;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestChat;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {
    public APIResponse addChat(APIRequestChat apiRequestChat, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse deleteChat(Long id) throws Exception;
}
