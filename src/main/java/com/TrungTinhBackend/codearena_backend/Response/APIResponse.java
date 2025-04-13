package com.TrungTinhBackend.codearena_backend.Response;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    private Long statusCode;

    private String message;

    private Object data;

    private String token;

    private LocalDateTime timestamp;

    public APIResponse(long l, String s, Object o, LocalDateTime now) {
    }
}
