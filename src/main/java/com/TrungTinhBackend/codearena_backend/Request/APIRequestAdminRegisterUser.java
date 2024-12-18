package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIRequestAdminRegisterUser {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String img;

    private LocalDateTime birthDay;

    private String address;

    private RoleEnum roleEnum;

    private LocalDateTime date;

}
