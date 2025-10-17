package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Bill;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.ImportedBook;

@Repository
public interface ImportedBookRepository extends JpaRepository<ImportedBook, Long> {
    List<ImportedBook> findByBook(Book book);

    List<ImportedBook> findByBill(Bill bill);
}
