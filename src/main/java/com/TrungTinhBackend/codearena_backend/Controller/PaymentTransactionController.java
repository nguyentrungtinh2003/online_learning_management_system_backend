package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.PaymentTransactionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.PaymentTransactionService;
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

    @Autowired
    private PaymentTransactionService paymentTransactionService;

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

    @GetMapping("/execute/vnpay")
    public ResponseEntity<APIResponse> executePaymentVNPay(
            HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayService.executePayment(request));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getPaymentByPage(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.getPaymentByPage(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getPaymentById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.getPaymentById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getPaymentByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.getPaymentByUserId(userId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deletePayment(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.deletePayment(id));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<APIResponse> restorePayment(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.restorePayment(id));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchPayment(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(paymentTransactionService.searchPayment(keyword, page, size));
    }
}
