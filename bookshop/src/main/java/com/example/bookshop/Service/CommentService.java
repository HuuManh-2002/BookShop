package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Comment;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.CommentReponse;
import com.example.bookshop.EntityDto.Request.CommentRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.CommentMapper;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserService userService;

    public List<CommentReponse> getByBook(Long id) {
        List<Comment> comments = commentRepository.findByBook_id(id);
        List<CommentReponse> commentReponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentReponses.add(commentMapper.toCommentReponse(comment));
        }
        return commentReponses;
    }

    public CommentReponse create(CommentRequest commentRequest) {
        User user = userService.getUserformToKen();
        Comment comment = commentMapper.toComment(commentRequest);
        Book book = bookRepository.findById(commentRequest.getBook_id())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        comment.setBook(book);
        comment.setUser(user);
        commentRepository.save(comment);
        return commentMapper.toCommentReponse(comment);
    }

    public void deleteMyComment(Long id) {
        User user = userService.getUserformToKen();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        commentRepository.delete(comment);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

}
