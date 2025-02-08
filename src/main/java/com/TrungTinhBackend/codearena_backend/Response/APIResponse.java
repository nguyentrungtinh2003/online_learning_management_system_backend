package com.TrungTinhBackend.codearena_backend.Response;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIResponse {

    private Long statusCode;

    private String message;

    private Object data;

    private String token;

    private LocalDateTime timestamp;
}
