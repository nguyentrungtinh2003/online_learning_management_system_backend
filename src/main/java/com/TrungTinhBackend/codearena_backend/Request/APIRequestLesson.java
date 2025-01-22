package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class APIRequestLesson {

    private Long id;

    private String lessonName;

    private String description;

    private String img;

    private LocalDateTime date;

    private String videoURL;

    private boolean isDeleted;

    private Course course;

    private List<Quiz> quizzes;
}
