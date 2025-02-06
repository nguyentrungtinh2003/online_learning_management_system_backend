package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentTransactionService {
    public APIResponse handlePayment(APIRequestPaymentTransaction apiRequestPaymentTransaction) throws Exception;
}
