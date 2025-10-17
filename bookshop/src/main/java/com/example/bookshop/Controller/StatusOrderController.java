package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.StatusOrderReponse;
import com.example.bookshop.Service.StatusOrderService;

@RestController
@RequestMapping("/statusoder")
public class StatusOrderController {

    @Autowired
    StatusOrderService statusOrderService;

    @GetMapping("/{order_id}")
    public ApiReponse<List<StatusOrderReponse>> getByOrder(@PathVariable Long order_id) {
        return ApiReponse.<List<StatusOrderReponse>>builder()
                .code(1000)
                .result(statusOrderService.getByOrder(order_id))
                .build();
    }

    @GetMapping("/me/{order_id}")
    public ApiReponse<List<StatusOrderReponse>> getByMyOrder(@PathVariable Long order_id) {
        return ApiReponse.<List<StatusOrderReponse>>builder()
                .code(1000)
                .result(statusOrderService.getByMyOrder(order_id))
                .build();
    }

}
