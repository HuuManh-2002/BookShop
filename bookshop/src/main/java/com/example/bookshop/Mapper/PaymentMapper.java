package com.example.bookshop.Mapper;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Payment;
import com.example.bookshop.EntityDto.Reponse.PaymentReponse;
import com.example.bookshop.EntityDto.Request.PaymentRequest;

@Service
public class PaymentMapper {

    public PaymentReponse toPaymentReponse(Payment payment) {
        return PaymentReponse.builder()
                .id(payment.getId())
                .name(payment.getName())
                .description(payment.getDescription())
                .build();
    }

    public Payment toPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .name(paymentRequest.getName())
                .description(paymentRequest.getDescription())
                .build();
    }
}
