package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookshop.Entity.Bill;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.ImportedBook;
import com.example.bookshop.Entity.Supplier;
import com.example.bookshop.EntityDto.Reponse.BillReponse;
import com.example.bookshop.EntityDto.Request.BillRequest;
import com.example.bookshop.EntityDto.Request.ImportedBookRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.BillMapper;
import com.example.bookshop.Mapper.ImportedBookMapper;
import com.example.bookshop.Repository.BillRepository;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.ImportedBookRepository;
import com.example.bookshop.Repository.SupplierRepository;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ImportedBookRepository importedBookRepository;

    @Autowired
    BillMapper billMapper;

    @Autowired
    ImportedBookMapper importedBookMapper;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BillReponse> getAll() {
        List<BillReponse> billReponses = new ArrayList<>();
        List<Bill> bills = billRepository.findAll();
        for (Bill bill : bills) {
            billReponses.add(billMapper.toBillReponse(bill));
        }
        return billReponses;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public BillReponse get(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_FOUND));
        return billMapper.toBillReponse(bill);
    }
    
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BillReponse> getBySupplier(Long supplier_id) {

        Supplier supplier = supplierRepository.findById(supplier_id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        List<Bill> bills = billRepository.findBySupplier(supplier);
        List<BillReponse> billReponses = new ArrayList<>();

        for (Bill bill : bills) {
            billReponses.add(billMapper.toBillReponse(bill));
        }
        return billReponses;
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public BillReponse create(BillRequest billRequest) {
        Supplier supplier = supplierRepository.findById(billRequest.getSupplier_id())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        Bill bill = billMapper.toBill(billRequest);
        bill.setSupplier(supplier);

        billRepository.save(bill);

        List<ImportedBookRequest> importedBookRequests = billRequest.getImportedBookRequest();
        for (ImportedBookRequest importedBookRequest : importedBookRequests) {
            ImportedBook importedBook = importedBookMapper.toImportedBook(importedBookRequest);
            Book book = bookRepository.findById(importedBookRequest.getBook_id())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
            book.setQuantity(book.getQuantity() + importedBook.getQuantity());
            importedBook.setBook(book);
            importedBook.setBill(bill);
            importedBookRepository.save(importedBook);
        }
        return billMapper.toBillReponse(bill);

    }
}
