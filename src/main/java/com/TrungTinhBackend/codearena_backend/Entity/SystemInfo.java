package com.TrungTinhBackend.codearena_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String systemName;

    private String slogan;

    private String img;

    private String address;

    private String phoneNumber;

    private String email;

    private LocalDateTime date;
    private LocalDateTime updateDate;

    private String socialMediaURL;

    private String description;

    private boolean isDeleted;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
