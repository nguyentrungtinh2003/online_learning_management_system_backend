package com.TrungTinhBackend.codearena_backend.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserResetPasswordDTO {

    @NotBlank(message = "Email is required !")
    @Email(message = "Invalid email format!")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be in a valid format (e.g., example@gmail.com)"
    )
    private String email;

    @NotNull(message = "OTP required !")
    @Size(min = 6,max = 6, message = "Invalid OTP !")
    private String otp;

    @NotBlank(message = "Password is required !")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character!"
    )
    private String password;
}
