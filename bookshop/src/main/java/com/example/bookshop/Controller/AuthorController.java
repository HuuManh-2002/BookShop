package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.AuthorReponse;
import com.example.bookshop.EntityDto.Request.AuthorRequest;
import com.example.bookshop.EntityDto.Update.AuthorUpdate;
import com.example.bookshop.Service.AuthorService;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @GetMapping()
    public ApiReponse<List<AuthorReponse>> getAll() {
        return ApiReponse.<List<AuthorReponse>>builder()
                .code(1000)
                .result(authorService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<AuthorReponse> getById(@PathVariable Long id) {
        return ApiReponse.<AuthorReponse>builder()
                .code(1000)
                .result(authorService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<AuthorReponse> create(@RequestBody AuthorRequest authorRequest) {
        return ApiReponse.<AuthorReponse>builder()
                .code(1000)
                .result(authorService.create(authorRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<AuthorReponse> update(@PathVariable Long id, @RequestBody AuthorUpdate authorUpdate) {

        return ApiReponse.<AuthorReponse>builder()
                .code(1000)
                .result(authorService.update(authorUpdate, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        authorService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}
