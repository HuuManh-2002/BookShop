package com.example.bookshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Order;
import com.example.bookshop.Entity.StatusOrder;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.StatusOrderReponse;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.StatusOrderMapper;
import com.example.bookshop.Repository.OrderRepository;
import com.example.bookshop.Repository.StatusOrderRepository;

@Service
public class StatusOrderService {

    @Autowired
    StatusOrderMapper statusOrderMapper;
    @Autowired
    StatusOrderRepository statusOrderRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<StatusOrderReponse> getByOrder(Long order_id) {
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<StatusOrder> statusOrders = statusOrderRepository.findByOrderOrderByUpdateTimeAsc(order);
        return statusOrderMapper.toListStatusOrderReponse(statusOrders);
    }

    public List<StatusOrderReponse> getByMyOrder(Long order_id){
        User user = userService.getUserformToKen();
        Order order = orderRepository.findById(order_id)
                .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(order.getUser().getId() != user.getId()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        List<StatusOrder> statusOrders = statusOrderRepository.findByOrderOrderByUpdateTimeAsc(order);
        return statusOrderMapper.toListStatusOrderReponse(statusOrders);
    }
}
