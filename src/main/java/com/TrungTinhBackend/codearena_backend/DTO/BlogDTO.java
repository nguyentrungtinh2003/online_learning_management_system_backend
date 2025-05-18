package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BlogDTO {

    private Long id;

    @NotBlank(message = "Blog name is required !")
    @Size(min = 2, max = 255, message = "Blog name must be between 2 and 255 characters!")
    private String blogName;

    @NotBlank(message = "Description is required !")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters!")
    private String description;

    private Long userId;
    private String username;

    private String userImg;

    private LocalDateTime date;

    private Set<Long> likedUsers;

    private boolean isDeleted;

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
    private String video;

}
