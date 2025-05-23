package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminRegisterUserDTO {

    @NotBlank(message = "Username is required !")
    @Size(min = 1, max = 255, message = "Username must be between 1 and 255 characters!")
    private String username;

    @NotBlank(message = "Password is required !")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character!"
    )
    private String password;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be in a valid format (e.g., example@gmail.com)"
    )
    private String email;

    @NotBlank(message = "Phone number is required!")
    @Pattern(
            regexp = "^(\\+84|0)[3-9][0-9]{8}$",
            message = "Invalid phone number! Must be a valid Vietnamese phone number (e.g., 0987654321 or +84987654321)"
    )
    private String phoneNumber;

    @NotBlank(message = "Image URL is required!")
    @Pattern(
            regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif|bmp|webp))$",
            message = "Invalid image URL! Must be a valid link ending with png, jpg, jpeg, gif, bmp, or webp."
    )
    private String img;

    @NotNull(message = "Birthday is required !")
    @Past(message = "Birthday must be in the past !")
    private LocalDate birthDay;

    @NotBlank(message = "Address is required !")
    @Size(min = 4, max = 255, message = "Address must be between 4 and 255 characters!")
    private String address;

    @NotNull(message = "Role is required !")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;
}
