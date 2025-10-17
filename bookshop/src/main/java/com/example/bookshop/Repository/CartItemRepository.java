package com.example.bookshop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.CartItem;
import com.example.bookshop.Entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndBook(User user, Book book);
}
