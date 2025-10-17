package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Supplier;
import com.example.bookshop.EntityDto.Reponse.SupplierReponse;
import com.example.bookshop.EntityDto.Request.SupplierRequest;

@Service
public class SupplierMapper {

    public SupplierReponse toSupplierReponse(Supplier supplier) {
        return SupplierReponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .description(supplier.getDescription())
                .createdTime(supplier.getCreatedTime())
                .build();
    }

    public Supplier toSupplier(SupplierRequest supplierRequest) {
        return Supplier.builder()
                .name(supplierRequest.getName())
                .description(supplierRequest.getDescription())
                .createdTime(LocalDateTime.now())
                .build();
    }
}
