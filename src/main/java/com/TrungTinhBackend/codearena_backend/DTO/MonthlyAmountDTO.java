package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.Data;

@Data
public class MonthlyAmountDTO {
    private int month;
    private Double amount;

    public MonthlyAmountDTO(int month, Double amount) {
    }
}
