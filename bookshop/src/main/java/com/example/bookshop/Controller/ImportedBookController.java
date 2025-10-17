package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.ImportedBookReponse;
import com.example.bookshop.Service.ImportedBookService;

@RequestMapping("/importedbook")
@RestController
public class ImportedBookController {

    @Autowired
    ImportedBookService importedBookService;

    @GetMapping("/book/{book_id}")
    public ApiReponse<List<ImportedBookReponse>> getByBook(@PathVariable Long book_id) {
        return ApiReponse.<List<ImportedBookReponse>>builder()
                .code(1000)
                .result(importedBookService.getByBook(book_id))
                .build();
    }

    @GetMapping("/bill/{bill_id}")
    public ApiReponse<List<ImportedBookReponse>> getByBill(@PathVariable Long bill_id) {
        return ApiReponse.<List<ImportedBookReponse>>builder()
                .code(1000)
                .result(importedBookService.getByBill(bill_id))
                .build();
    }

}
