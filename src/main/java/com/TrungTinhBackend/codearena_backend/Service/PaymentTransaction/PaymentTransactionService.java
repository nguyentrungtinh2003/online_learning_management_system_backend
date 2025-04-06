package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentTransactionService {
    APIResponse createPayment(APIRequestPaymentTransaction request) throws Exception;
    APIResponse executePayment(String paymentId, String payerId, Long userId) throws Exception;
}
