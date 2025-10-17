package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.BookOrderReponse;
import com.example.bookshop.Service.BookOrderService;

@RestController
@RequestMapping("/bookorder")
public class BookOrderController {

    @Autowired
    BookOrderService bookOrderService;

    @GetMapping("/{order_id}")
    public ApiReponse<List<BookOrderReponse>> getByOrder(@PathVariable Long order_id) {
        return ApiReponse.<List<BookOrderReponse>>builder()
                .code(1000)
                .result(bookOrderService.getByOrder(order_id))
                .build();
    }

    @GetMapping("/me/{order_id}")
    public ApiReponse<List<BookOrderReponse>> getByMyOrder(@PathVariable Long order_id) {
        return ApiReponse.<List<BookOrderReponse>>builder()
                .code(1000)
                .result(bookOrderService.getByMyOrder(order_id))
                .build();
    }

}
