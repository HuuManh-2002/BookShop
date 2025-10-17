package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Author;
import com.example.bookshop.EntityDto.Reponse.AuthorReponse;
import com.example.bookshop.EntityDto.Request.AuthorRequest;
import com.example.bookshop.EntityDto.Update.AuthorUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.AuthorMapper;
import com.example.bookshop.Repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    AuthorMapper authorMapper;

    public List<AuthorReponse> getAll() {

        List<Author> authors = authorRepository.findAll();
        List<AuthorReponse> authorReponses = new ArrayList<>();
        for (Author author : authors) {
            authorReponses.add(authorMapper.toAuthorReponse(author));
        }
        return authorReponses;
    }

    public AuthorReponse get(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return authorMapper.toAuthorReponse(author);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public AuthorReponse create(AuthorRequest authorRequest) {
        Author author = authorMapper.toAuthor(authorRequest);
        authorRepository.save(author);
        return authorMapper.toAuthorReponse(author);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public AuthorReponse update(AuthorUpdate authorUpdate, Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        author.setName(authorUpdate.getName());
        author.setGender(authorUpdate.getGender());
        author.setDescription(authorUpdate.getDescription());
        author.setYob(authorUpdate.getYob());

        authorRepository.save(author);
        return authorMapper.toAuthorReponse(author);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AppException(ErrorCode.AUTHOR_NOT_FOUND);
        }
        authorRepository.deleteById(id);
    }
}
