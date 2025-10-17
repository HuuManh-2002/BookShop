package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Order;
import com.example.bookshop.Entity.StatusOrder;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrder, Long> {
    List<StatusOrder> findByOrderOrderByUpdateTimeAsc(Order order);
}
