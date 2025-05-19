package com.TrungTinhBackend.codearena_backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private boolean success;
    private String message;
    private LocalDateTime timestamp;

    public LoginLog(String username, String ipAddress, boolean success, String message) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.success = success;
        this.message = message;
    }
}
