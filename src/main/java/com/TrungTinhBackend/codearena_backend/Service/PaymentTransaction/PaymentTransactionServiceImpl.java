package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction;

import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.PaymentTransactionRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.PaymentTransactionSpecification;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService{

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public APIResponse getPaymentByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get payment by page success !");
        apiResponse.setData(paymentTransactions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getPaymentById(Long id) {
        APIResponse apiResponse = new APIResponse();

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Payment not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get payment by id success !");
        apiResponse.setData(paymentTransaction);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getPaymentByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get payment by userId success !");
        apiResponse.setData(paymentTransactions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getTotalAmount() {
        APIResponse apiResponse = new APIResponse();

        Double totalAmount = paymentTransactionRepository.getTotalAmount();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get total amount success !");
        apiResponse.setData(totalAmount);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse deletePayment(Long id) {
        APIResponse apiResponse = new APIResponse();

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Payment not found !")
        );

        paymentTransaction.setDeleted(true);
        paymentTransactionRepository.save(paymentTransaction);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete payment by id success !");
        apiResponse.setData(paymentTransaction);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse restorePayment(Long id) {
        APIResponse apiResponse = new APIResponse();

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Payment not found !")
        );

        paymentTransaction.setDeleted(false);
        paymentTransactionRepository.save(paymentTransaction);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore payment by id success !");
        apiResponse.setData(paymentTransaction);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse searchPayment(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<PaymentTransaction> specification = PaymentTransactionSpecification.searchByKeyword(keyword);

        Page<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search payment success");
        apiResponse.setData(paymentTransactions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
