package com.TrungTinhBackend.codearena_backend.Service.PaymentTransaction.VNPay;

import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.PaymentTransactionStatus;
import com.TrungTinhBackend.codearena_backend.Repository.PaymentTransactionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.PaymentTransactionDTO;
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

    private final UserRepository userRepository;

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final String vnpUrl;
    private final String vnpReturnUrl;
    private final String vnpTmnCode;
    private final String vnpHashSecret;

    private static final double COIN_RATE = 10.0;
    private static final String ORDER_INFO = "Nạp tiền vào CodeArena";

    public VNPayServiceImpl(
            UserRepository userRepository,
            PaymentTransactionRepository paymentTransactionRepository,
            @Value("${vnp_Url}") String vnpUrl,
            @Value("${VNP_RETURN_URL}") String vnpReturnUrl,
            @Value("${vnp_TmnCode}") String vnpTmnCode,
            @Value("${vnp_HashSecret}") String vnpHashSecret) {
        this.userRepository = userRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.vnpUrl = vnpUrl;
        this.vnpReturnUrl = vnpReturnUrl;
        this.vnpTmnCode = vnpTmnCode;
        this.vnpHashSecret = vnpHashSecret;
    }



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
    public APIResponse createPayment(HttpServletRequest request, PaymentTransactionDTO requestDTO) throws Exception {
        APIResponse response = new APIResponse();

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();

        String orderInfo = ORDER_INFO;
        String orderType = "other";
        long amount = (long) (requestDTO.getAmount() * 100); // VND x 100

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnp_TxnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl + "?userId=" + requestDTO.getUserId());
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

        String vnp_SecureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        response.setStatusCode(200L);
        response.setMessage("Redirect to VNPay");
        response.setData(vnpUrl + "?" + query);
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
