package com.example.bookshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "book_order")

public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int quantity;
    int total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @OneToOne(mappedBy = "book_order")
    Rate rate;

}
