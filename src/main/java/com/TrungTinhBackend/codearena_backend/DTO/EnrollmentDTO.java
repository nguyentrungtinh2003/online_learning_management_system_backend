package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseName;
    private LocalDateTime enrolledDate;
    private String status;
    private int progress;
}
