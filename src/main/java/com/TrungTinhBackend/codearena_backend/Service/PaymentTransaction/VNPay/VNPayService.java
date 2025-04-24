package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.VNPay;

import com.TrungTinhBackend.codearena_backend.DTO.PaymentTransactionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    APIResponse createPayment(HttpServletRequest request, PaymentTransactionDTO requestDTO) throws Exception;
    APIResponse executePayment(HttpServletRequest request);
}
