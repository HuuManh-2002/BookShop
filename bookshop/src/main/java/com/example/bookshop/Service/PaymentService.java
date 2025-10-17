package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Payment;
import com.example.bookshop.EntityDto.Reponse.PaymentReponse;
import com.example.bookshop.EntityDto.Request.PaymentRequest;
import com.example.bookshop.EntityDto.Update.PaymentUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.PaymentMapper;
import com.example.bookshop.Repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper paymentMapper;

    public List<PaymentReponse> getAll() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentReponse> paymentReponses = new ArrayList<>();
        for (Payment payment : payments) {
            paymentReponses.add(paymentMapper.toPaymentReponse(payment));
        }
        return paymentReponses;
    }

    public PaymentReponse get(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentReponse(payment);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public PaymentReponse create(PaymentRequest paymentRequest) {
        Payment payment = paymentMapper.toPayment(paymentRequest);
        paymentRepository.save(payment);
        return paymentMapper.toPaymentReponse(payment);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public PaymentReponse update(PaymentUpdate paymentUpdate, Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.setName(paymentUpdate.getName());

        paymentRepository.save(payment);
        return paymentMapper.toPaymentReponse(payment);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        paymentRepository.deleteById(id);
    }
}
