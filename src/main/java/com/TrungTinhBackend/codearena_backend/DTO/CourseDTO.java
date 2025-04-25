package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.CourseEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseDTO {

    @NotBlank(message = "Course name is required !")
    @Size(min = 2, max = 255, message = "Course name must be between 2 and 255 characters!")
    private String courseName;

    @NotBlank(message = "Description is required !")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters!")
    private String description;

//    @NotBlank(message = "Image URL is required!")
//    @Pattern(
//            regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif|bmp|webp))$",
//            message = "Invalid image URL! Must be a valid link ending with png, jpg, jpeg, gif, bmp, or webp."
//    )
//    private String img;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0!")
    @DecimalMax(value = "1000000000", message = "Price must be less than 1 billion!")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid number with up to 2 decimal places!")
    private Double price;

    private CourseEnum courseEnum;

//    private List<User> users;

    private Long userId;

//    private List<Lesson> lessons;
}
