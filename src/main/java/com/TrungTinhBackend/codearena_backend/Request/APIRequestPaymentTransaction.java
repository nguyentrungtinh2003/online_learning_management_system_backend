package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.PaymentTransactionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIRequestPaymentTransaction {

    private Long id;

    private User user;

    private double amount;

}
