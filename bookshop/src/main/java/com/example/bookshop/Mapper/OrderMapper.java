package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Order;
import com.example.bookshop.EntityDto.Reponse.OrderReponse;
import com.example.bookshop.EntityDto.Request.OrderRequest;

@Service
public class OrderMapper {

    @Autowired
    StatusOrderMapper statusOrderMapper;
    @Autowired
    BookOrderMapper bookOrderMapper;

    public Order toOrder(OrderRequest orderRequest) {
        return Order.builder()
                .total(orderRequest.getTotal())
                .createdTime(LocalDateTime.now())
                .build();
    }

    public OrderReponse toOrderReponse(Order order) {
        return OrderReponse.builder()
                .id(order.getId())
                .total(order.getTotal())
                .currentStatus(order.getCurrentStatus())
                .createdTime(order.getCreatedTime())
                .build();
    }
}
