package com.example.bookshop.EntityDto.Request;

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
public class BookRequest {
    String name;
    int pageNumber;
    int publishYear;
    int price;
    String description;
    
    Long category_id;
    Long publisher_id;
    Long author_id;

}
