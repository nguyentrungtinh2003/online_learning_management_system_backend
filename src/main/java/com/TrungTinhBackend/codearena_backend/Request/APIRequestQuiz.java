package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class APIRequestQuiz {

    private Long id;

    private String quizName;

    private String img;

    private LocalDateTime date;

    private String description;

    private boolean isDeleted;

    private Lesson lesson;

    private List<Question> questions;
}
