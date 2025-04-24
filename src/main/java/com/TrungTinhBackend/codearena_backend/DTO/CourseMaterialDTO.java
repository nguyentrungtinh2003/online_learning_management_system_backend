package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

@Data
public class CourseMaterialDTO {
    private String title;
    private String description;

    private Course course;

    private User lecturer;
}
