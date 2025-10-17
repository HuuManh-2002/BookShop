package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Supplier;
import com.example.bookshop.EntityDto.Reponse.SupplierReponse;
import com.example.bookshop.EntityDto.Request.SupplierRequest;
import com.example.bookshop.EntityDto.Update.SupplierUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.SupplierMapper;
import com.example.bookshop.Repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierMapper supplierMapper;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<SupplierReponse> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierReponse> statusReponses = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            statusReponses.add(supplierMapper.toSupplierReponse(supplier));

        }
        return statusReponses;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SupplierReponse get(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
        return supplierMapper.toSupplierReponse(supplier);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SupplierReponse create(SupplierRequest supplierRequest) {
        Supplier supplier = supplierMapper.toSupplier(supplierRequest);
        supplierRepository.save(supplier);
        return supplierMapper.toSupplierReponse(supplier);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SupplierReponse update(Long id, SupplierUpdate supplierUpdate) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));

        supplier.setName(supplierUpdate.getName());
        supplier.setDescription(supplierUpdate.getDescription());

        supplierRepository.save(supplier);
        return supplierMapper.toSupplierReponse(supplier);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        supplierRepository.delete(supplier);
    }
}
