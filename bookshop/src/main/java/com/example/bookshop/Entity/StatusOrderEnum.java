package com.example.bookshop.Entity;

import lombok.Getter;

@Getter
public enum StatusOrderEnum {

    PENDING_PAYMENT(1L, "Pending Payment", "The order has been created but not yet paid."),
    PENDING_CONFIRMATION(2L, "Pending Confirmation", "The payment has been made and is awaiting confirmation."),
    PROGRESSING(3L, "Progressing", "The order is being processed and prepared for delivery."),
    DELIVERING(4L, "Delivering", "The order is out for delivery to the customer."),
    COMPLETED(5L, "Completed", "The order has been delivered and completed successfully."),
    CANCELED(6L, "Canceled", "The order has been canceled."),
    REJECT(7L, "Reject", "The order has been rejected due to issues."),
    RETURN(8L, "Return", "The order is being returned by the customer.");

    StatusOrderEnum(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    private Long id;
    private String name;
    private String description;
}
