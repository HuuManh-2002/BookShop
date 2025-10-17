package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Comment;
import com.example.bookshop.EntityDto.Reponse.CommentReponse;
import com.example.bookshop.EntityDto.Request.CommentRequest;

@Service
public class CommentMapper {
    public CommentReponse toCommentReponse(Comment comment) {
        return CommentReponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .build();
    }

    public Comment toComment(CommentRequest commentRequest) {
        return Comment.builder()
                .content(commentRequest.getContent())
                .createdTime(LocalDateTime.now())
                .build();
    }
}
