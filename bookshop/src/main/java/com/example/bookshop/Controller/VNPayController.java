package com.example.bookshop.Controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.VNPayTransactionResponse;
import com.example.bookshop.EntityDto.Reponse.VNPayURLResponse;
import com.example.bookshop.EntityDto.Request.VNPayRequest;
import com.example.bookshop.Service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    @Autowired
    VNPayService vnpayService;

    @GetMapping("/create_payment")
    public ApiReponse<VNPayURLResponse> createPayment(HttpServletRequest req, @RequestBody VNPayRequest vnPayRequest)
            throws UnsupportedEncodingException {

        return ApiReponse.<VNPayURLResponse>builder()
                .code(1000)
                .message("Success")
                .result(vnpayService.createPayment(req, vnPayRequest))
                .build();
    }

    @GetMapping("/transaction_return")
    public ApiReponse<VNPayTransactionResponse> vnpayReturn(
            @RequestParam("vnp_Amount") Long vnpAmount,
            @RequestParam("vnp_ResponseCode") String vnpResponseCode,
            @RequestParam("vnp_TransactionStatus") String vnpTransactionStatus,
            @RequestParam("vnp_TxnRef") String vnpOrderId


    ) throws UnsupportedEncodingException {
        return ApiReponse.<VNPayTransactionResponse>builder()
                .code(1000)
                .message("Success")
                .result(vnpayService.vnpayReturn(vnpAmount, vnpResponseCode, vnpTransactionStatus, vnpOrderId))
                .build();
    }
}
