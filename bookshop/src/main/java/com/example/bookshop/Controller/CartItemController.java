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
import com.example.bookshop.EntityDto.Reponse.CartItemReponse;
import com.example.bookshop.EntityDto.Request.CartItemRequest;
import com.example.bookshop.EntityDto.Update.CartItemUpdate;
import com.example.bookshop.Service.CartItemService;

@RestController
@RequestMapping("/cartitem")
public class CartItemController {

    @Autowired
    CartItemService cartItemService;

    @GetMapping()
    public ApiReponse<List<CartItemReponse>> getAll() {
        return ApiReponse.<List<CartItemReponse>>builder()
                .code(1000)
                .result(cartItemService.getAll())
                .build();
    }

    @PostMapping()
    public ApiReponse<CartItemReponse> create(@RequestBody CartItemRequest cartItemRequest) {
        return ApiReponse.<CartItemReponse>builder()
                .code(1000)
                .result(cartItemService.create(cartItemRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<CartItemReponse> update(@PathVariable Long id, @RequestBody CartItemUpdate cartItemUpdate) {
        return ApiReponse.<CartItemReponse>builder()
                .code(1000)
                .result(cartItemService.update(cartItemUpdate, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {
        cartItemService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
