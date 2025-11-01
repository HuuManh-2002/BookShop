package com.example.bookshop.Entity;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {

    BANK_TRANFER(1L, "Bank Transfer", "Thanh toan qua tai khoan ngan hang"),
    COD(2L, "Cash on Delivery", "Thanh toan khi nhan hang");

    PaymentMethodEnum(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    private Long id;
    private String name;
    private String description;
}
