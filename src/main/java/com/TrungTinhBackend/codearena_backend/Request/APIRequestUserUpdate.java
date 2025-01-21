package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class APIRequestUserUpdate {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String img;

    private LocalDate birthDay;

    private String address;

    private Long point;

    private Long coin;

    private RankEnum rankEnum;

    private RoleEnum roleEnum;

    private StatusUserEnum statusUserEnum;

    private LocalDateTime date;

    private boolean enabled;
}
