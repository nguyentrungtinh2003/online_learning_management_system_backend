package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlogComment;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.PaymentTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/payments")
public class PaymentTransactionController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPayment(@Valid @RequestBody APIRequestPaymentTransaction apiRequestPaymentTransaction) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.handlePayment(apiRequestPaymentTransaction));
    }
}
