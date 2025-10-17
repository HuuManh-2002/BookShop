package com.example.bookshop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Like;
import com.example.bookshop.Entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByUser(User user);

    List<Like> findByBook(Book book);

    Optional<Like> findByBookAndUser(Book book, User user);

    Optional<Like> findByBook_id(Long book_id);
}
