package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.example.bookshop.EntityDto.Reponse.BookReponse;
import com.example.bookshop.EntityDto.Request.BookRequest;
import com.example.bookshop.EntityDto.Update.BookUpdate;
import com.example.bookshop.Service.BookService;

@RequestMapping("/book")
@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public Page<BookReponse> getAllBooks(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/publisher/{publisher_id}")
    public ApiReponse<List<BookReponse>> getByPublisher(@PathVariable Long publisher_id) {
        return ApiReponse.<List<BookReponse>>builder()
                .code(1000)
                .result(bookService.getByPublisher(publisher_id))
                .build();
    }

    @GetMapping("/author/{author_id}")
    public ApiReponse<List<BookReponse>> getByAuthor(@PathVariable Long author_id) {
        return ApiReponse.<List<BookReponse>>builder()
                .code(1000)
                .result(bookService.getByAuthor(author_id))
                .build();
    }

    @GetMapping("/category/{category_id}")
    public ApiReponse<List<BookReponse>> get(@PathVariable Long category_id) {
        return ApiReponse.<List<BookReponse>>builder()
                .code(1000)
                .result(bookService.getByCategory(category_id))
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<BookReponse> getById(@PathVariable Long id) {
        return ApiReponse.<BookReponse>builder()
                .code(1000)
                .result(bookService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<BookReponse> create(
            @RequestPart("bookRequest") BookRequest bookRequest,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        return ApiReponse.<BookReponse>builder()
                .code(1000)
                .result(bookService.create(bookRequest, avatarFile))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<BookReponse> update(
            @PathVariable("id") Long id,
            @RequestPart("bookUpdate") BookUpdate bookUpdate,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        return ApiReponse.<BookReponse>builder()
                .code(1000)
                .result(bookService.update(id, bookUpdate, avatarFile))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
