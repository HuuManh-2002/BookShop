package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.CartItem;
import com.example.bookshop.EntityDto.Reponse.CartItemReponse;
import com.example.bookshop.EntityDto.Request.CartItemRequest;

@Service
public class CartItemMapper {

    public CartItem toCartItem(CartItemRequest cartItemRequest) {
        return CartItem.builder()
                .quantity(cartItemRequest.getQuantity())
                .createdTime(LocalDateTime.now())
                .build();
    }

    public CartItemReponse toCartItemReponse(CartItem cartItem) {
        return CartItemReponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .createdTime(cartItem.getCreatedTime())
                .build();
    }
}
