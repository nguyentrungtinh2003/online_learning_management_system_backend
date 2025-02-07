package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.PaymentTransactionStatus;
import com.TrungTinhBackend.codearena_backend.Repository.PaymentTransactionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService{

    @Autowired
    private APIContext apiContext;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    private static final double COIN_RATE = 10.0; // 1 USD = 10 Coin

    @Override
    public APIResponse handlePayment(APIRequestPaymentTransaction apiRequestPaymentTransaction) throws Exception {
        APIResponse apiResponse = new APIResponse();
            // Tạo thanh toán
            Payment payment = createPayment(apiRequestPaymentTransaction.getAmount());

// Xử lý thanh toán sau khi người dùng thanh toán qua PayPal
            if (payment.getState().equals("approved")) {
                double paymentAmount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
                Double coinAmount = (paymentAmount * COIN_RATE);

// Cập nhật số dư coin cho người dùng
                Optional<User> userOpt = userRepository.findById(apiRequestPaymentTransaction.getUser().getId());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setCoin(user.getCoin() + coinAmount);
                    userRepository.save(user);

// Lưu thông tin giao dịch
                    PaymentTransaction transaction = new PaymentTransaction();
                    transaction.setUser(user);
                    transaction.setAmount(paymentAmount);
                    transaction.setCoinAmount(coinAmount);
                    transaction.setStatus(PaymentTransactionStatus.COMPLETED);
                    paymentTransactionRepository.save(transaction);
                }
            }
            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Payment success !");
            apiResponse.setData(payment);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    private Payment createPayment(Double total) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Nạp tiền vào tài khoản");
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:3000/cancel");
        redirectUrls.setReturnUrl("http://localhost:3000/success");
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
}