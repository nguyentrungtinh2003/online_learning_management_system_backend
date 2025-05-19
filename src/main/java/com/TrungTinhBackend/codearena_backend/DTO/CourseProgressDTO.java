package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgressDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private String img;
    private int completedLessons;
    private int totalLessons;
    private int progressPercent;
}
