package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Author;
import com.example.bookshop.EntityDto.Reponse.AuthorReponse;
import com.example.bookshop.EntityDto.Request.AuthorRequest;

@Service
public class AuthorMapper {

    public AuthorReponse toAuthorReponse(Author author) {
        return AuthorReponse.builder()
                .id(author.getId())
                .name(author.getName())
                .yob(author.getYob())
                .gender(author.getGender())
                .description(author.getDescription())
                .createTime(author.getCreateTime())
                .build();
    }

    public Author toAuthor(AuthorRequest authorRequest) {
        return Author.builder()
                .name(authorRequest.getName())
                .yob(authorRequest.getYob())
                .gender(authorRequest.getGender())
                .description(authorRequest.getDescription())
                .createTime(LocalDateTime.now())
                .build();
    }
}
