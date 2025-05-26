package com.TrungTinhBackend.codearena_backend.Entity;

import com.TrungTinhBackend.codearena_backend.Enum.QuizEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizName;

    private String img;

    private Double price;

    private QuizEnum quizEnum;

    private LocalDateTime date;
    private LocalDateTime updateDate;

    private String description;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    @JsonIgnore()
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz")
    @JsonManagedReference()
    private List<Question> questions;

    @ManyToMany
    @JoinTable(
            name = "quiz_user_submit",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userSubmit = new ArrayList<>();

}
