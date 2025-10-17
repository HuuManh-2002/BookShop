package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findByBook(Book book);
}
