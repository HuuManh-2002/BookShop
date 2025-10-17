package com.example.bookshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "imported_book")
public class ImportedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int quantity;
    int cost;
    int total;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    Bill bill;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;
    
}
