package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Like;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.LikeReponse;
import com.example.bookshop.EntityDto.Request.LikeRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.LikeMapper;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.LikeRepository;
import com.example.bookshop.Repository.UserRepository;

@Service
public class LikeService {

    @Autowired
    LikeRepository likeRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    LikeMapper likeMapper;

    public LikeReponse create(LikeRequest likeRequest) {
        User user = userService.getUserformToKen();
        Book book = bookRepository.findById(likeRequest.getBook_id())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (likeRepository.findByBookAndUser(book, user).isPresent()) {
            throw new AppException(ErrorCode.LIKED_BOOK_AGO);
        }
        Like like = likeMapper.toLike(likeRequest);
        like.setUser(user);
        like.setBook(book);
        likeRepository.save(like);
        book.setLikeNumber(book.getLikeNumber() + 1);
        bookRepository.save(book);
        return likeMapper.toLikeReponse(like);
    }

    public void delete(Long book_id) {
        User user = userService.getUserformToKen();
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        Like like = likeRepository.findByBook_id(book_id)
                .orElseThrow(() -> new AppException(ErrorCode.LIKE_NOT_FOUND));
        if (like.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        likeRepository.delete(like);
        book.setLikeNumber(book.getLikeNumber() - 1);
        bookRepository.save(book);

    }

    public List<LikeReponse> getByBook(Long book_id) {
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        List<Like> likes = likeRepository.findByBook(book);
        List<LikeReponse> likeReponses = new ArrayList<>();
        for (Like like : likes) {
            likeReponses.add(likeMapper.toLikeReponse(like));
        }
        return likeReponses;
    }
}
