package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.BookOrder;
import com.example.bookshop.Entity.Order;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByOrder(Order order);
}
