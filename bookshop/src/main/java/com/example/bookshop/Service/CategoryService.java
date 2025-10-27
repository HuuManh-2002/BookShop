package com.example.bookshop.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookshop.Entity.Category;
import com.example.bookshop.EntityDto.Reponse.CategoryReponse;
import com.example.bookshop.EntityDto.Request.CategoryRequest;
import com.example.bookshop.EntityDto.Update.CategoryUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.CategoryMapper;
import com.example.bookshop.Repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    FileStorageService fileStorageService;

    public List<CategoryReponse> getAll() {
        List<Category> categorys = categoryRepository.findAll();
        List<CategoryReponse> categoryReponses = new ArrayList<>();
        for (Category category : categorys) {
            categoryReponses.add(categoryMapper.toCategoryReponse(category));
        }
        return categoryReponses;
    }

    public CategoryReponse get(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryReponse(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CategoryReponse create(CategoryRequest categoryRequest, MultipartFile avatarFile) {
        Category category = categoryMapper.toCategory(categoryRequest);

        String savedFileName = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                savedFileName = fileStorageService.saveFile(avatarFile, "image/category/");
            } catch (IOException e) {
                // Bắt IOException và ném ra AppException
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (savedFileName != null) {
            category.setSavedFileName(savedFileName);
        }
        categoryRepository.save(category);
        return categoryMapper.toCategoryReponse(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CategoryReponse update(CategoryUpdate categoryUpdate, Long id, MultipartFile avatarFile) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setName(categoryUpdate.getName());
        category.setDescription(categoryUpdate.getDescription());
        
        try {
            fileStorageService.deleteFile(category.getSavedFileName(), "image/category/");
        } catch (IOException e) {
            throw new AppException(ErrorCode.DETETE_FILE_FAILED);
        }

        String savedFileName = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                savedFileName = fileStorageService.saveFile(avatarFile, "image/category/");
            } catch (IOException e) {
                // Bắt IOException và ném ra AppException
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (savedFileName != null) {
            category.setSavedFileName(savedFileName);
        }
        categoryRepository.save(category);
        return categoryMapper.toCategoryReponse(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        try {
            fileStorageService.deleteFile(category.getSavedFileName(), "image/category/");

        } catch (IOException e) {
            throw new AppException(ErrorCode.DETETE_FILE_FAILED);
        }
        categoryRepository.deleteById(id);
    }
}
