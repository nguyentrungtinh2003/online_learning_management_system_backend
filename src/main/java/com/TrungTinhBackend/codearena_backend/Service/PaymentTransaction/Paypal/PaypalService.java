package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.Paypal;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface PaypalService {
    APIResponse createPayment(APIRequestPaymentTransaction request) throws Exception;
    APIResponse executePayment(String paymentId, String payerId, Long userId) throws Exception;
}
