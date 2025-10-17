package com.example.bookshop.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "book")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    int pageNumber;
    int publishYear;
    int price;
    int quantity;
    int likeNumber;
    float start;
    int purchaseNumber;
    int voteNumber;
    String description;
    LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    Publisher publisher;

    @OneToMany(mappedBy = "book")
    List<ImportedBook> imported_books;

    @OneToMany(mappedBy = "book")
    List<BookOrder> book_orders;

    @OneToMany(mappedBy = "book")
    List<Like> likes;

    @OneToMany(mappedBy = "book")
    List<Comment> comments;

    @OneToMany(mappedBy = "book")
    List<CartItem> cart_items;

    @OneToMany(mappedBy = "book")
    List<Rate> rates;
}
