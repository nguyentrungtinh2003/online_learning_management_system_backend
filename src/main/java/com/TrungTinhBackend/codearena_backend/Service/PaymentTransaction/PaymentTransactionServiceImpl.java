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

    public PaymentTransactionServiceImpl(APIContext apiContext, PaymentTransactionRepository paymentTransactionRepository, UserRepository userRepository) {
        this.apiContext = apiContext;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.userRepository = userRepository;
    }


    @Override
    public APIResponse createPayment(APIRequestPaymentTransaction request) throws Exception {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f",request.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setDescription("Nạp tiền vào tài khoản CodeArena");
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://codearena-frontend-dev.vercel.app/payment-cancel");
        redirectUrls.setReturnUrl("https://codearena-frontend-dev.vercel.app/payment-success?userId=" + request.getUserId());
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        String approvalLink = createdPayment.getLinks().stream()
                .filter(link -> link.getRel().equalsIgnoreCase("approval_url"))
                .findFirst().map(Links::getHref).orElse(null);

        APIResponse response = new APIResponse();
        response.setStatusCode(200L);
        response.setMessage("Redirect to PayPal");
        response.setData(approvalLink);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public APIResponse executePayment(String paymentId, String payerId, Long userId) throws Exception {
        try {
            // Khởi tạo đối tượng Payment từ paymentId
            Payment payment = new Payment();
            payment.setId(paymentId);

            // Khởi tạo PaymentExecution để thực hiện thanh toán
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            // Thực hiện thanh toán với PayPal
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            // Kiểm tra trạng thái thanh toán
            if (executedPayment.getState().equals("approved")) {
                double paymentAmount = Double.parseDouble(executedPayment.getTransactions().get(0).getAmount().getTotal());

                // Kiểm tra COIN_RATE hợp lệ
                if (COIN_RATE <= 0) {
                    throw new Exception("Invalid coin rate.");
                }

                double coinAmount = paymentAmount * COIN_RATE;

                // Lấy người dùng từ database
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setCoin(user.getCoin() + coinAmount);
                    userRepository.save(user);

                    // Lưu thông tin giao dịch vào database
                    PaymentTransaction transaction = new PaymentTransaction();
                    transaction.setUser(user);
                    transaction.setAmount(paymentAmount);
                    transaction.setCoinAmount(coinAmount);
                    transaction.setStatus(PaymentTransactionStatus.COMPLETED);
                    paymentTransactionRepository.save(transaction);

                    // Trả về thông tin thành công
                    APIResponse response = new APIResponse();
                    response.setStatusCode(200L);
                    response.setMessage("Payment Success and Coins Added");
                    response.setData(executedPayment);
                    response.setTimestamp(LocalDateTime.now());
                    return response;
                } else {
                    throw new Exception("User not found.");
                }
            } else {
                throw new Exception("Payment not approved.");
            }
        } catch (PayPalRESTException e) {
            throw new Exception("PayPal Payment Execution failed: " + e.getMessage(), e);
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            throw new Exception("An error occurred while processing the payment: " + e.getMessage(), e);
        }
    }
}