package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPointHistoryDTO {
    private Long userId;
    private Long point;
}
