package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.RateReponse;
import com.example.bookshop.EntityDto.Request.RateRequest;
import com.example.bookshop.Service.RateService;

@RequestMapping("/rate")
@RestController
public class RateController {

    @Autowired
    RateService rateService;

    @GetMapping("/{book_id}")
    public ApiReponse<List<RateReponse>> getByBook(@PathVariable Long book_id) {
        return ApiReponse.<List<RateReponse>>builder()
                .code(1000)
                .result(rateService.getByBook(book_id))
                .build();
    }

    @PostMapping("/{bookOrder_id}")
    public ApiReponse<RateReponse> create(@RequestBody RateRequest rateRequest, @PathVariable Long bookOrder_id) {

        return ApiReponse.<RateReponse>builder()
                .code(1000)
                .result(rateService.create(rateRequest, bookOrder_id))
                .build();
    }

}
