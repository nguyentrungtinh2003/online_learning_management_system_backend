package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatDTO {

    private ChatRoom chatRoom;

    private Long senderId;

    @NotBlank(message = "Message is required !")
    @Size(min = 0, max = 255, message = "Message must be between 0 and 255 characters!")
    private String message;

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

}
