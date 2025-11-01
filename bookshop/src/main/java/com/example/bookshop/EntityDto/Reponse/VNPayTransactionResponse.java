package com.example.bookshop.EntityDto.Reponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VNPayTransactionResponse {
    long amount; // Số tiền thực tế (đã chia cho 100)
    String bankCode; // Mã ngân hàng
    String status; // Trạng thái đơn hàng (SUCCESS, FAILED, PENDING)
    String orderId; // Thông tin đơn hàng
    // Hàm tiện ích để chuyển đổi ResponseCode và TransactionStatus sang Status

}
