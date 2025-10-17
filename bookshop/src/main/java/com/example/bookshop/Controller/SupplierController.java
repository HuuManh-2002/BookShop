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
import com.example.bookshop.EntityDto.Reponse.SupplierReponse;
import com.example.bookshop.EntityDto.Request.SupplierRequest;
import com.example.bookshop.EntityDto.Update.SupplierUpdate;
import com.example.bookshop.Service.SupplierService;

@RequestMapping("/supplier")
@RestController
public class SupplierController {
    @Autowired
    SupplierService supplierService;

    @GetMapping()
    public ApiReponse<List<SupplierReponse>> getAll() {
        return ApiReponse.<List<SupplierReponse>>builder()
                .code(1000)
                .result(supplierService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<SupplierReponse> getById(@PathVariable Long id) {
        return ApiReponse.<SupplierReponse>builder()
                .code(1000)
                .result(supplierService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<SupplierReponse> create(@RequestBody SupplierRequest supplierRequest) {
        return ApiReponse.<SupplierReponse>builder()
                .code(1000)
                .result(supplierService.create(supplierRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<SupplierReponse> update(@PathVariable Long id, @RequestBody SupplierUpdate supplierUpdate) {

        return ApiReponse.<SupplierReponse>builder()
                .code(1000)
                .result(supplierService.update(id, supplierUpdate))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        supplierService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}