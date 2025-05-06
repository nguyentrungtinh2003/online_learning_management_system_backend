package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemInfoDTO {
    private Long id;

    private String systemName;

    private String slogan;

    private String img;

    private String address;

    private String phoneNumber;

    private String email;

    private LocalDateTime date;

    private String socialMediaURL;

    private String description;

    private boolean isDeleted;

    private Long userId;
}
