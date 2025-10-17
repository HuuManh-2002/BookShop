package com.example.bookshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Bill;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.ImportedBook;
import com.example.bookshop.EntityDto.Reponse.ImportedBookReponse;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.ImportedBookMapper;
import com.example.bookshop.Repository.BillRepository;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.ImportedBookRepository;

@Service
public class ImportedBookService {

    @Autowired
    ImportedBookRepository importedBookRepository;

    @Autowired
    ImportedBookMapper importedBookMapper;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BillRepository billRepository;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<ImportedBookReponse> getByBook(Long book_id) {
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        List<ImportedBook> importedBooks = importedBookRepository.findByBook(book);
        return importedBookMapper.toListImportedBookReponse(importedBooks);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<ImportedBookReponse> getByBill(Long bill_id) {
        Bill bill = billRepository.findById(bill_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        List<ImportedBook> importedBooks = importedBookRepository.findByBill(bill);
        return importedBookMapper.toListImportedBookReponse(importedBooks);
    }

}
