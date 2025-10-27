
package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Category;
import com.example.bookshop.EntityDto.Reponse.CategoryReponse;
import com.example.bookshop.EntityDto.Request.CategoryRequest;

@Service
public class CategoryMapper {

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .createdTime(LocalDateTime.now())
                .build();
    }

    public CategoryReponse toCategoryReponse(Category category) {
        return CategoryReponse.builder()
                .id(category.getId())
                .savedFileName(category.getSavedFileName())
                .name(category.getName())
                .description(category.getDescription())
                .createdTime(category.getCreatedTime())
                .build();
    }
}
