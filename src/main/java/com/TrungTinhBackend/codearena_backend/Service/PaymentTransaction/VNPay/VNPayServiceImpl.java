package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.VNPay;

import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.PaymentTransactionStatus;
import com.TrungTinhBackend.codearena_backend.Repository.PaymentTransactionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestPaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VNPayServiceImpl implements VNPayService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Value("${vnp_Url}")
    private String VNP_URL;

    private String VNP_RETURN_URL="https://codearena-backend-dev.onrender.com";

    @Value("${vnp_TmnCode}")
    private String VNP_TMNCODE;

    @Value("${vnp_HashSecret}")
    private String VNP_HASH_SECRET;

    private static final double COIN_RATE = 10.0;

    public VNPayServiceImpl(UserRepository userRepository, PaymentTransactionRepository paymentTransactionRepository, String vnpUrl, String vnpTmncode, String vnpHashSecret) {
        this.userRepository = userRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        VNP_URL = vnpUrl;
        VNP_TMNCODE = vnpTmncode;
        VNP_HASH_SECRET = vnpHashSecret;
    }

    //hash
    public String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes());
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }
    //

    @Override
    public APIResponse createPayment(HttpServletRequest request, APIRequestPaymentTransaction requestDTO) throws Exception {
        APIResponse response = new APIResponse();

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();

        String orderInfo = "Nạp tiền vào CodeArena";
        String orderType = "other";
        long amount = (long) (requestDTO.getAmount() * 100); // VND x 100

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", VNP_TMNCODE);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnp_TxnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VNP_RETURN_URL + "?userId=" + requestDTO.getUserId());
        vnpParams.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cal = Calendar.getInstance();
        Date createdDate = cal.getTime();
        String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(createdDate);
        vnpParams.put("vnp_CreateDate", vnp_CreateDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }

        String vnp_SecureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        response.setStatusCode(200L);
        response.setMessage("Redirect to VNPay");
        response.setData(VNP_URL + "?" + query);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public APIResponse executePayment(HttpServletRequest request) {
        APIResponse response = new APIResponse();

        String responseCode = request.getParameter("vnp_ResponseCode");
        String amountStr = request.getParameter("vnp_Amount");
        String userIdStr = request.getParameter("userId");

        if ("00".equals(responseCode)) {
            double amountVND = Double.parseDouble(amountStr) / 100.0;
            double coinAmount = amountVND * COIN_RATE;

            Optional<User> userOpt = userRepository.findById(Long.parseLong(userIdStr));
            if (userOpt.isEmpty()) {
                response.setStatusCode(404L);
                response.setMessage("User not found !");
                response.setData(null);
                response.setTimestamp(LocalDateTime.now());

                return response;
            }

            User user = userOpt.get();
            user.setCoin(user.getCoin() + coinAmount);
            userRepository.save(user);

            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setAmount(amountVND);
            transaction.setCoinAmount(coinAmount);
            transaction.setUser(user);
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus(PaymentTransactionStatus.COMPLETED);
            transaction.setDeleted(false);
            paymentTransactionRepository.save(transaction);

            response.setStatusCode(200L);
            response.setMessage("Thanh toán thành công !");
            response.setData(null);
            response.setTimestamp(LocalDateTime.now());

            return response;
        }

        response.setStatusCode(500L);
        response.setMessage("Thanh toán thất bại !");
        response.setData(null);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
