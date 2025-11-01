package com.example.bookshop.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Configuration.VNPayConfig;
import com.example.bookshop.Entity.Order;
import com.example.bookshop.EntityDto.Reponse.VNPayTransactionResponse;
import com.example.bookshop.EntityDto.Reponse.VNPayURLResponse;
import com.example.bookshop.EntityDto.Request.VNPayRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VNPayService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    // --- HÀM TẠO THANH TOÁN (Không đổi) ---
    public VNPayURLResponse createPayment(HttpServletRequest req, VNPayRequest vnPayRequest)
            throws UnsupportedEncodingException {

        Order order = orderRepository.findById(vnPayRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getUser().getId() != userService.getUserformToKen().getId()) {
            log.info("Order_user" + order.getUser().getId().toString());
            log.info("Token_user" + userService.getUserformToKen().getId().toString());

            throw new AppException(ErrorCode.CANNOT_PAYMENT);

        }
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = String.format("Thanh toan don hang {} tai BookShop", vnPayRequest.getOrderId());
        String orderType = "other";
        String vnp_TxnRef = vnPayRequest.getOrderId().toString();
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>(); // Dùng generic cho tường minh
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnPayRequest.getAmount() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "NCB"); // Có thể bỏ để người dùng chọn
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // ... (phần Billing/Invoice đã comment) ...

        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet()); // Dùng generic
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator(); // Dùng generic
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                // Quan trọng: Phải URL Encode dữ liệu thô khi tạo chuỗi hash
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        // **In ra chuỗi hash data thô để Debug**
        // System.out.println("Raw Hash Data (VNPay Create): " + hashData.toString());

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return VNPayURLResponse.builder()
                .status("100")
                .message("Success")
                .url(paymentUrl)
                .build();
    }

    // --- HÀM XÁC THỰC KẾT QUẢ TRẢ VỀ (ĐÃ SỬA) ---
    public VNPayTransactionResponse vnpayReturn(
            Long vnpAmount,
            String vnpResponseCode,
            String vnpTransactionStatus,
            String vnpOrderId) throws UnsupportedEncodingException {

        String status = setStatus(vnpResponseCode, vnpTransactionStatus);
        if (status.equals("PENDING") || status.equals("FAILED")) {
            throw new AppException(ErrorCode.CANNOT_PAYMENT);
        }
        orderService.progressUser(Long.valueOf(vnpOrderId));
        return VNPayTransactionResponse.builder()
                .amount(vnpAmount)
                .orderId(vnpOrderId)
                .status(status)
                .build();
    }

    public String setStatus(String vnpResponseCode, String vnpTransStatus) {
        if (vnpResponseCode.equals("00") && vnpTransStatus.equals("00")) {
            return "SUCCESS";
        } else if (vnpResponseCode.equals("00") && vnpTransStatus.equals("01")) {
            return "PENDING"; // Chờ xử lý
        } else {
            return "FAILED";
        }
    }
}