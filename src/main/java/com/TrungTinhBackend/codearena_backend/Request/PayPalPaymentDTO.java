package com.TrungTinhBackend.codearena_backend.Request;

import lombok.Data;

@Data
public class PayPalPaymentDTO {
    private String id;
    private String state;
    private String createTime;
}
