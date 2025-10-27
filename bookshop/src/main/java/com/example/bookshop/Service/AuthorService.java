package com.example.bookshop.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    FileStorageService fileStorageService;

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
    public AuthorReponse create(AuthorRequest authorRequest, MultipartFile avatarFile) {
        Author author = authorMapper.toAuthor(authorRequest);
        String savedFileName = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                savedFileName = fileStorageService.saveFile(avatarFile, "image/author");
            } catch (IOException e) {
                // Bắt IOException và ném ra AppException
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (savedFileName != null) {
            author.setSavedFileName(savedFileName);
        }

        authorRepository.save(author);
        return authorMapper.toAuthorReponse(author);

    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public AuthorReponse update(AuthorUpdate authorUpdate, Long id, MultipartFile avatarFile) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        author.setName(authorUpdate.getName());
        author.setGender(authorUpdate.getGender());
        author.setDescription(authorUpdate.getDescription());
        author.setYob(authorUpdate.getYob());
        try {
            fileStorageService.deleteFile(author.getSavedFileName(), "image/author/");

        } catch (IOException e) {
            throw new AppException(ErrorCode.DETETE_FILE_FAILED);
        }

        String savedFileName = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                savedFileName = fileStorageService.saveFile(avatarFile, "image/author/");
            } catch (IOException e) {
                // Bắt IOException và ném ra AppException
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (savedFileName != null) {
            author.setSavedFileName(savedFileName);
        }
        authorRepository.save(author);
        return authorMapper.toAuthorReponse(author);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        try {
            fileStorageService.deleteFile(author.getSavedFileName(), "image/author/");

        } catch (IOException e) {
            throw new AppException(ErrorCode.DETETE_FILE_FAILED);
        }
        authorRepository.deleteById(id);
    }
}
