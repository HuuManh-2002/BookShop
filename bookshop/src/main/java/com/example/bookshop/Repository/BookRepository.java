package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Author;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Category;
import com.example.bookshop.Entity.Publisher;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByPublisher(Publisher publisher);

    List<Book> findByCategory(Category category);

    List<Book> findByAuthor(Author author);
}
