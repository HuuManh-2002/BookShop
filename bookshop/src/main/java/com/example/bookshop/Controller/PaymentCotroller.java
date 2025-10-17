package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.PaymentReponse;
import com.example.bookshop.EntityDto.Request.PaymentRequest;
import com.example.bookshop.EntityDto.Update.PaymentUpdate;
import com.example.bookshop.Service.PaymentService;

@RequestMapping("/payment")
@RestController
public class PaymentCotroller {
    @Autowired
    PaymentService paymentService;

    @GetMapping()
    public ApiReponse<List<PaymentReponse>> getAll() {
        return ApiReponse.<List<PaymentReponse>>builder()
                .code(1000)
                .result(paymentService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<PaymentReponse> getById(@PathVariable Long id) {
        return ApiReponse.<PaymentReponse>builder()
                .code(1000)
                .result(paymentService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<PaymentReponse> create(@RequestBody PaymentRequest paymentRequest) {
        return ApiReponse.<PaymentReponse>builder()
                .code(1000)
                .result(paymentService.create(paymentRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<PaymentReponse> update(@PathVariable Long id, @RequestBody PaymentUpdate paymentUpdate) {

        return ApiReponse.<PaymentReponse>builder()
                .code(1000)
                .result(paymentService.update(paymentUpdate, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        paymentService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}
