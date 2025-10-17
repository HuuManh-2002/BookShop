package com.example.bookshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.BookOrder;
import com.example.bookshop.Entity.Order;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.BookOrderReponse;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.BookOrderMapper;
import com.example.bookshop.Repository.BookOrderRepository;
import com.example.bookshop.Repository.OrderRepository;

@Service
public class BookOrderService {

    @Autowired
    BookOrderRepository bookOrderRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookOrderMapper bookOrderMapper;

    @Autowired
    UserService userService;

    public List<BookOrderReponse> getByMyOrder(Long order_id) {
        User user = userService.getUserformToKen();
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        List<BookOrder> bookOrder = bookOrderRepository.findByOrder(order);
        return bookOrderMapper.toListStatusOrderReponse(bookOrder);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BookOrderReponse> getByOrder(Long order_id) {
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        List<BookOrder> bookOrder = bookOrderRepository.findByOrder(order);
        return bookOrderMapper.toListStatusOrderReponse(bookOrder);
    }
}
