package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.EntityDto.Reponse.BookReponse;
import com.example.bookshop.EntityDto.Request.BookRequest;

@Service
public class BookMapper {

    public BookReponse toBookReponse(Book book) {
        return BookReponse.builder()
                .id(book.getId())
                .name(book.getName())
                .savedFileName(book.getSavedFileName())
                .pageNumber(book.getPageNumber())
                .publishYear(book.getPublishYear())
                .description(book.getDescription())
                .likeNumber(book.getLikeNumber())
                .price(book.getPrice())
                .purchaseNumber(book.getPurchaseNumber())
                .voteNumber(book.getVoteNumber())
                .createdTime(book.getCreatedTime())
                .start(book.getStart())
                .quantity(book.getQuantity())
                .build();
    }

    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .name(bookRequest.getName())
                .pageNumber(bookRequest.getPageNumber())
                .price(bookRequest.getPrice())
                .createdTime(LocalDateTime.now())
                .publishYear(bookRequest.getPublishYear())
                .quantity(0)
                .purchaseNumber(0)
                .voteNumber(0)
                .likeNumber(0)
                .start(0)
                .description(bookRequest.getDescription())
                .build();
    }
}
