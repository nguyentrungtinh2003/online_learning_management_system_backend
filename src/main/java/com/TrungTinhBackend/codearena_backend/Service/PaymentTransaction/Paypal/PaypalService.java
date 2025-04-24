package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.Paypal;

import com.TrungTinhBackend.codearena_backend.DTO.PaymentTransactionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface PaypalService {
    APIResponse createPayment(PaymentTransactionDTO request) throws Exception;
    APIResponse executePayment(String paymentId, String payerId, Long userId) throws Exception;
}
