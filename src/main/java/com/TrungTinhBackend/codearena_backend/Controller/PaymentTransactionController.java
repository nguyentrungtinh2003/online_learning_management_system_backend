package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.PaymentTransactionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.Paypal.PaypalService;
import com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.VNPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/payments")
public class PaymentTransactionController {

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createPayment(HttpServletRequest request,@Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO) throws Exception {
        if("PayPal".equalsIgnoreCase(paymentTransactionDTO.getMethod())) {
            return ResponseEntity.ok(paypalService.createPayment(paymentTransactionDTO));
        }
        if("VNPay".equalsIgnoreCase(paymentTransactionDTO.getMethod())) {
            return ResponseEntity.ok(vnPayService.createPayment( request,paymentTransactionDTO));
        }
        return ResponseEntity.internalServerError().body(new APIResponse(500L,"Phương thức thanh toán không hợp lệ",null,LocalDateTime.now()));
    }

    @GetMapping("/execute/paypal")
    public ResponseEntity<APIResponse> executePaymentPayPal(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId,
            @RequestParam("userId") Long userId) throws Exception {
        return ResponseEntity.ok(paypalService.executePayment(paymentId, payerId, userId));
    }

    @PostMapping("/execute/vnpay")
    public ResponseEntity<APIResponse> executePaymentVNPay(
            HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayService.executePayment(request));
    }
}
