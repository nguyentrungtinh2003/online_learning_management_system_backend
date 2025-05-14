package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface PaymentTransactionService {
    public APIResponse getPaymentByPage(int page, int size);
    public APIResponse getPaymentById(Long id);
    public APIResponse getPaymentByUserId(Long userId);
    public APIResponse getTotalAmount();
    public APIResponse deletePayment(Long id);
    public APIResponse restorePayment(Long id);
    public APIResponse searchPayment(String keyword, int page, int size);
}
