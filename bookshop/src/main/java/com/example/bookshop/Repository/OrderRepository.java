package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Order;
import com.example.bookshop.Entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByCurrentStatus(Long currentStatus);

    List<Order> findByUserAndCurrentStatus(User user, Long currentStatus);
}
