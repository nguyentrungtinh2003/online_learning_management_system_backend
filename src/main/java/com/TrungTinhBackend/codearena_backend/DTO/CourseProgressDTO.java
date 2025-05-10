package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.Data;

@Data
public class CourseProgressDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private String img;
    private int completedLessons;
    private int totalLessons;
    private int progressPercent;

    public CourseProgressDTO(Long id, String courseName, int completedLessons, int totalLessons, int percent) {
    }

    public CourseProgressDTO() {
    }

    public CourseProgressDTO(Long id, String courseName, String description, String img, int completedLessons, int totalLessons, int percent) {
    }
}
