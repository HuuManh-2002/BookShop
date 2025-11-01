package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.OrderReponse;
import com.example.bookshop.EntityDto.Request.DescriptionRequest;
import com.example.bookshop.EntityDto.Request.OrderRequest;
import com.example.bookshop.Service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping()
    public ApiReponse<List<OrderReponse>> getAll() {
        return ApiReponse.<List<OrderReponse>>builder()
                .code(1000)
                .result(orderService.getAll())
                .build();
    }

    @GetMapping("/status/{status_id}")
    public ApiReponse<List<OrderReponse>> getByStatus(@PathVariable Long status_id) {
        return ApiReponse.<List<OrderReponse>>builder()
                .code(1000)
                .result(orderService.getByStatus(status_id))
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<OrderReponse> get(@PathVariable Long id) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.get(id))
                .build();
    }

    @GetMapping("/me")
    public ApiReponse<List<OrderReponse>> getAllMyOrder() {
        return ApiReponse.<List<OrderReponse>>builder()
                .code(1000)
                .result(orderService.getAllMyOrder())
                .build();
    }

    @GetMapping("me/{id}")
    public ApiReponse<OrderReponse> getMyOder(@PathVariable Long id) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.getMyOrder(id))
                .build();
    }

    @GetMapping("/me/status/{status_id}")
    public ApiReponse<List<OrderReponse>> getMyOrderByStatus(@PathVariable Long status_id) {
        return ApiReponse.<List<OrderReponse>>builder()
                .code(1000)
                .result(orderService.getMyOrderByStatus(status_id))
                .build();
    }

    @PostMapping()
    public ApiReponse<OrderReponse> create(@RequestBody OrderRequest orderRequest) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.create(orderRequest))
                .build();
    }

    @PutMapping("/progress/{id}")
    public ApiReponse<OrderReponse> progressAdmin(@PathVariable Long id) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.progressAdmin(id))
                .build();
    }

    @PutMapping("/delivery/{id}")
    public ApiReponse<OrderReponse> delivery(@PathVariable Long id) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.delivery(id))
                .build();
    }

    @PutMapping("/return/{id}")
    public ApiReponse<OrderReponse> reject(@PathVariable Long id) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.returnBook(id))
                .build();
    }

    @PutMapping("/complele/{id}")
    public ApiReponse<OrderReponse> complele(@PathVariable Long id, @RequestBody DescriptionRequest descriptionRequest) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.complele(id, descriptionRequest.getContent()))
                .build();
    }


    @PutMapping("/cancel/{id}")
    public ApiReponse<OrderReponse> cancel(@PathVariable Long id, @RequestBody DescriptionRequest descriptionRequest) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.cancel(id, descriptionRequest.getContent()))
                .build();
    }

    @PutMapping("/reject/{id}")
    public ApiReponse<OrderReponse> reject(@PathVariable Long id, @RequestBody DescriptionRequest descriptionRequest) {
        return ApiReponse.<OrderReponse>builder()
                .code(1000)
                .result(orderService.reject(id, descriptionRequest.getContent()))
                .build();
    }

}
