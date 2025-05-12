package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class LessonDTO {

    @NotBlank(message = "Lesson name is required !")
    @Size(min = 2, max = 255, message = "Lesson name must be between 2 and 255 characters!")
    private String lessonName;

    @NotBlank(message = "Description is required !")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters!")
    private String description;

    @NotBlank(message = "Image URL is required!")
    @Pattern(
            regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif|bmp|webp))$",
            message = "Invalid image URL! Must be a valid link ending with png, jpg, jpeg, gif, bmp, or webp."
    )
    private String img;

    @NotBlank(message = "Video URL is required!")
    @Pattern(
            regexp = "^(https?:\\/\\/.*\\.(?:mp4|mkv|avi|mov|wmv|flv|webm))$\n",
            message = "Invalid video URL! Must be a valid link ending with mp4, mkv, avi, mov, wmv, fly or webm."
    )
    private String videoURL;

    private Long courseId;

    private List<Long> quizzes;
}
