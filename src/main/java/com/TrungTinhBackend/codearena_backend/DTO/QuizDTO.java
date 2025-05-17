package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Enum.QuizEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizDTO {

    private Long id;

    @NotBlank(message = "Quiz name is required !")
    @Size(min = 2, max = 255, message = "Quiz name must be between 2 and 255 characters!")
    private String quizName;

    @NotBlank(message = "Image URL is required!")
    @Pattern(
            regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif|bmp|webp))$",
            message = "Invalid image URL! Must be a valid link ending with png, jpg, jpeg, gif, bmp, or webp."
    )
    private String img;

    @NotNull(message = "Price is required!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0!")
    @DecimalMax(value = "1000000000", message = "Price must be less than 1 billion!")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid number with up to 2 decimal places!")
    private Double price;

    private QuizEnum quizEnum;

    @NotBlank(message = "Description is required !")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters!")
    private String description;

    private Long lessonId;

    private String LessonName;

    private LocalDateTime date;
    private LocalDateTime updateDate;

    private List<Question> questions;

    private List<Long> userIdSubmit;
}
