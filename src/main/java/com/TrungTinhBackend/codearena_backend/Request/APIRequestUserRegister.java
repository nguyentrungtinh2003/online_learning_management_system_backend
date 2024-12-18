package com.TrungTinhBackend.codearena_backend.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class APIRequestUserRegister {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Email is mandatory")
    private String email;
}
