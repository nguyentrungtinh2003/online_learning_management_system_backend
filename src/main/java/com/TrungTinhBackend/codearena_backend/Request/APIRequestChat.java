package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIRequestChat {

    private Long id;

    private ChatRoom chatRoom;

    private User sender;

    private String message;

    private String img;

    private String videoURL;

}
