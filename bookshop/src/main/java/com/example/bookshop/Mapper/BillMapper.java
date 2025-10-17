package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Bill;
import com.example.bookshop.EntityDto.Reponse.BillReponse;
import com.example.bookshop.EntityDto.Request.BillRequest;

@Service
public class BillMapper {

    @Autowired
    ImportedBookMapper importedBookMapper;

    public BillReponse toBillReponse(Bill bill) {
        return BillReponse.builder()
                .id(bill.getId())
                .total(bill.getTotal())
                .createTime(LocalDateTime.now())
                .description(bill.getDescription())
                .build();
    }

    public Bill toBill(BillRequest billRequest) {
        return Bill.builder()
                .total(billRequest.getTotal())
                .description(billRequest.getDescription())
                .build();
    }
}
