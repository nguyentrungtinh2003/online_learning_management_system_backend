package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionDTO {

    @NotBlank(message = "Question name is required !")
    @Size(min = 1, max = 255, message = "Question name must be between 1 and 255 characters!")
    private String questionName;

    @NotBlank(message = "Answer A is required !")
    @Size(min = 1, max = 255, message = "Answer A must be between 1 and 255 characters!")
    private String answerA;

    @NotBlank(message = "Answer B is required !")
    @Size(min = 1, max = 255, message = "Answer B must be between 1 and 255 characters!")
    private String answerB;

    @NotBlank(message = "Answer C is required !")
    @Size(min = 1, max = 255, message = "Answer C must be between 1 and 255 characters!")
    private String answerC;

    @NotBlank(message = "Answer D is required !")
    @Size(min = 1, max = 255, message = "Answer D must be between 1 and 255 characters!")
    private String answerD;

    @NotBlank(message = "Answer correct is required !")
    @Size(min = 1, max = 255, message = "Answer correct must be between 1 and 255 characters!")
    private String answerCorrect;

    @NotBlank(message = "Image URL is required!")
    @Pattern(
            regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif|bmp|webp))$",
            message = "Invalid image URL! Must be a valid link ending with png, jpg, jpeg, gif, bmp, or webp."
    )
    private String img;

    private Long quizId;
}
