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
import com.example.bookshop.EntityDto.Reponse.LikeReponse;
import com.example.bookshop.EntityDto.Request.LikeRequest;
import com.example.bookshop.Service.LikeService;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeService likeService;

    @GetMapping("/{book_id}")
    public ApiReponse<List<LikeReponse>> getByBook(@PathVariable Long book_id) {
        return ApiReponse.<List<LikeReponse>>builder()
                .code(1000)
                .result(likeService.getByBook(book_id))
                .build();
    }

    @PostMapping()
    public ApiReponse<LikeReponse> create(@RequestBody LikeRequest likeRequest) {
        return ApiReponse.<LikeReponse>builder()
                .code(1000)
                .result(likeService.create(likeRequest))
                .build();
    }

    @DeleteMapping("/{book_id}")
    public ApiReponse<String> delete(@PathVariable Long book_id) {
        likeService.delete(book_id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
