package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AnswerUserDTO {
    private Long quizId;

    private Long userId;

    private List<String> answersUser;
}
