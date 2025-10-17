package com.example.bookshop.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.BookOrder;
import com.example.bookshop.EntityDto.Reponse.BookOrderReponse;
import com.example.bookshop.EntityDto.Request.BookOrderRequest;

@Service
public class BookOrderMapper {

    public BookOrder toBookOrder(BookOrderRequest bookOrderRequest) {
        return BookOrder.builder()
                .quantity(bookOrderRequest.getQuantity())
                .total(bookOrderRequest.getTotal())
                .build();
    }

    public BookOrderReponse toBookOrderReponse(BookOrder bookOrder) {
        return BookOrderReponse.builder()
                .id(bookOrder.getId())
                .quantity(bookOrder.getQuantity())
                .total(bookOrder.getTotal())
                .build();
    }
    public List<BookOrderReponse> toListStatusOrderReponse(List<BookOrder> bookOrders){
        List<BookOrderReponse> bookOrderReponses = new ArrayList<>();
        for (BookOrder bookOrder : bookOrders) {
            bookOrderReponses.add(toBookOrderReponse(bookOrder));
        }
        return bookOrderReponses;
    }
}
