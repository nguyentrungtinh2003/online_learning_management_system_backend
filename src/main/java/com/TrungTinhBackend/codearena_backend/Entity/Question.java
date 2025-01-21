package com.TrungTinhBackend.codearena_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionName;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private String answerCorrect;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
