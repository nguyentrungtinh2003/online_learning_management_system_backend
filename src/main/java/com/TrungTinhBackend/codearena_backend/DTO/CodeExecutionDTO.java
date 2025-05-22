package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeExecutionDTO {
    private Long id;

    private String language;

    private String code;

    private String output;

    private LocalDateTime executedAt;

    private Long userId;

}
