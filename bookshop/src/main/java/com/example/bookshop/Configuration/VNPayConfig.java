package com.example.bookshop.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class VNPayConfig {

    // THÔNG TIN CẤU HÌNH CỦA BẠN (Đảm bảo SECRET KEY là chính xác)
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8000/api/vnpay/transaction_return";
    public static String vnp_TmnCode = "E8NZWLV6";
    public static String secretKey = "0K80PX3EL2WNVPTN4H8MF2G7F1GGDSCJ"; // **ĐẢM BẢO CHÍNH XÁC**
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    // --- CÁC HÀM HASH (Không đổi) ---
    public static String md5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    // --- HÀM TẠO CHUỖI HASH ĐỂ XÁC THỰC (ĐÃ SỬA) ---
    public static String hashAllFields(Map fields) {
        // Lấy danh sách các key và sắp xếp A-Z
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder finalHashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = (String) fields.get(fieldName);

            // Chỉ nối các trường có giá trị (không null, không rỗng)
            if (fieldValue != null && fieldValue.length() > 0) {
                // Thêm dấu & trước nếu chuỗi hashData đã có dữ liệu
                if (finalHashData.length() > 0) {
                    finalHashData.append("&");
                }
                finalHashData.append(fieldName);
                finalHashData.append("=");
                // KHÔNG URL ENCODE Ở ĐÂY!
                finalHashData.append(fieldValue);
            }
        }

        // **In ra chuỗi hash data thô để Debug**
        // System.out.println("Raw Hash Data (VNPay Return): " +
        // finalHashData.toString());

        return hmacSHA512(secretKey, finalHashData.toString());
    }

    // Hàm HMAC-SHA512 (Không đổi)
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            // Quan trọng: Sử dụng UTF-8 cho dữ liệu cần băm
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    // public static String getRandomNumber(int len) {
    //     Random rnd = new Random();
    //     String chars = "0123456789";
    //     StringBuilder sb = new StringBuilder(len);
    //     for (int i = 0; i < len; i++) {
    //         sb.append(chars.charAt(rnd.nextInt(chars.length())));
    //     }
    //     return sb.toString();
    // }
}