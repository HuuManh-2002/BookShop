package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.CategoryReponse;
import com.example.bookshop.EntityDto.Request.CategoryRequest;
import com.example.bookshop.EntityDto.Update.CategoryUpdate;
import com.example.bookshop.Service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping()
    public ApiReponse<List<CategoryReponse>> getAll() {
        return ApiReponse.<List<CategoryReponse>>builder()
                .code(1000)
                .result(categoryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<CategoryReponse> getById(@PathVariable Long id) {
        return ApiReponse.<CategoryReponse>builder()
                .code(1000)
                .result(categoryService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<CategoryReponse> create(
            @RequestPart("categoryRequest") CategoryRequest categoryRequest,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        return ApiReponse.<CategoryReponse>builder()
                .code(1000)
                .result(categoryService.create(categoryRequest, avatarFile))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<CategoryReponse> update(
            @PathVariable("id") Long id,
            @RequestPart("categoryUpdate") CategoryUpdate categoryUpdate,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {

        return ApiReponse.<CategoryReponse>builder()
                .code(1000)
                .result(categoryService.update(categoryUpdate, id, avatarFile))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        categoryService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}
