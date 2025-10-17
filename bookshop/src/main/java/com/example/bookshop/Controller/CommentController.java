package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.CommentReponse;
import com.example.bookshop.EntityDto.Request.CommentRequest;
import com.example.bookshop.Service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/{id}")
    public ApiReponse<List<CommentReponse>> getAllByBook(@PathVariable Long id) {
        return ApiReponse.<List<CommentReponse>>builder()
                .code(1000)
                .result(commentService.getByBook(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<CommentReponse> create(@RequestBody @Valid CommentRequest commentRequest) {
        return ApiReponse.<CommentReponse>builder()
                .code(1000)
                .result(commentService.create(commentRequest))
                .build();
    }

    @DeleteMapping("/me/{id}")
    public ApiReponse<String> deleteMyComment(@PathVariable Long id) {
        commentService.deleteMyComment(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
