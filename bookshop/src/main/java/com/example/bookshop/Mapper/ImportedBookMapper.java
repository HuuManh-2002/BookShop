package com.example.bookshop.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.ImportedBook;
import com.example.bookshop.EntityDto.Reponse.ImportedBookReponse;
import com.example.bookshop.EntityDto.Request.ImportedBookRequest;

@Service
public class ImportedBookMapper {

    public ImportedBookReponse toImportedBookReponse(ImportedBook importedBook) {
        return ImportedBookReponse.builder()
                .id(importedBook.getId())
                .quantity(importedBook.getQuantity())
                .cost(importedBook.getCost())
                .total(importedBook.getTotal())
                .build();
    }

    public ImportedBook toImportedBook(ImportedBookRequest importedBookRequest) {
        return ImportedBook.builder()
                .quantity(importedBookRequest.getQuantity())
                .cost(importedBookRequest.getCost())
                .total(importedBookRequest.getTotal())
                .build();
    }

    public List<ImportedBookReponse> toListImportedBookReponse(List<ImportedBook> importedBooks){
        List<ImportedBookReponse> importedBookReponses = new ArrayList<>();
        for (ImportedBook importedBook : importedBooks) {
            importedBookReponses.add(toImportedBookReponse(importedBook));
        }
        return importedBookReponses;
    }
}
