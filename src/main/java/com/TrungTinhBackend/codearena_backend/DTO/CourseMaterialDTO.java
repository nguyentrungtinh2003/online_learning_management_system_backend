package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

@Data
public class CourseMaterialDTO {
    private Long id;
    private String title;
    private String description;

    private Long courseId;
    private String courseName;

    private Long lecturerId;
    private String lecturerName;
}
