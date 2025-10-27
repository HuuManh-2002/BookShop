package com.example.bookshop.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_EXISTED(1001, "Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    PASSWORD_SHORT(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_MISSING_ALPHANUMERIC(1005, "Password must contain both letters and numbers", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(1005, "Address not found", HttpStatus.NOT_FOUND),
    AUTHOR_NOT_FOUND(1006, "Author not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1007, "Category not found", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_FOUND(1008, "Payment not found", HttpStatus.NOT_FOUND),
    SUPPLIER_NOT_FOUND(1009, "Supplier not found", HttpStatus.NOT_FOUND),
    STATUS_NOT_FOUND(1010, "Status not found", HttpStatus.NOT_FOUND),
    BOOK_NOT_FOUND(1011, "Book not found", HttpStatus.NOT_FOUND),
    PUBLISHER_NOT_FOUND(1012, "Publisher not found", HttpStatus.NOT_FOUND),
    BILL_NOT_FOUND(1013, "Bill not found", HttpStatus.NOT_FOUND),
    BOOK_ENOUGH_QUANTITY(1014, "The book is out of stock or not in enough quantity.", HttpStatus.NOT_FOUND),
    CARTITEM_NOT_FOUND(1015, "Cartitem not found", HttpStatus.NOT_FOUND),
    LIKE_NOT_FOUND(1016, "Like not found", HttpStatus.NOT_FOUND),
    LIKED_BOOK_AGO(1017, "Liked book ago", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(1018, "Comment not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1019, "Order not found", HttpStatus.NOT_FOUND),
    BOOK_ORDER_NOT_FOUND(1020, "BookOrder not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_COMPLETED(1021, "My Order not Completed", HttpStatus.BAD_REQUEST),
    UNABLE_UPDATE_ORDER(1022, "Unable to update order", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_MEDIA_TYPE(1023, "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_UPLOAD_FAILED(1024, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DETETE_FILE_FAILED(1025, "Delete file failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(7777, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND(8888, "Resource not found", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR);

    private ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
