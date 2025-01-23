package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class APIRequestQuestion {

    private Long id;

    private String questionName;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private String answerCorrect;

    private String img;

    private boolean isDeleted;

    private Quiz quiz;
}
