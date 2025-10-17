package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Comment;
import com.example.bookshop.Entity.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);

    List<Comment> findByBook_id(Long book_id);
}
