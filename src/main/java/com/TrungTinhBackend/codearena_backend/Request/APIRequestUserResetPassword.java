package com.TrungTinhBackend.codearena_backend.Request;

import lombok.Data;

@Data
public class APIRequestUserResetPassword {
    private String email;
    private String otp;
    private String password;
}
