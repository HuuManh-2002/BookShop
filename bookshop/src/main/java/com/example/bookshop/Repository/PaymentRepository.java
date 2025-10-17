package com.example.bookshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
