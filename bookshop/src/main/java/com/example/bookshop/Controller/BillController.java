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
import com.example.bookshop.EntityDto.Reponse.BillReponse;
import com.example.bookshop.EntityDto.Request.BillRequest;
import com.example.bookshop.Service.BillService;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BillService billService;

    @GetMapping()
    public ApiReponse<List<BillReponse>> getAll() {
        return ApiReponse.<List<BillReponse>>builder()
                .code(1000)
                .result(billService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<BillReponse> getById(@PathVariable Long id) {
        return ApiReponse.<BillReponse>builder()
                .code(1000)
                .result(billService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<BillReponse> create(@RequestBody BillRequest billRequest) {
        return ApiReponse.<BillReponse>builder()
                .code(1000)
                .result(billService.create(billRequest))
                .build();
    }

}
