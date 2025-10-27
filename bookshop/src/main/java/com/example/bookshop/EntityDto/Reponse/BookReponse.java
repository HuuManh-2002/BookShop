package com.example.bookshop.EntityDto.Reponse;

import java.time.LocalDateTime;

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
public class BookReponse {
    Long id;
    String name;
    String savedFileName;
    int pageNumber;
    int publishYear;
    int price;
    int quantity;
    int likeNumber;
    float start;
    int purchaseNumber;
    int voteNumber;
    LocalDateTime createdTime;
    String description;

}
