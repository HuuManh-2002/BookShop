package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Like;
import com.example.bookshop.EntityDto.Reponse.LikeReponse;
import com.example.bookshop.EntityDto.Request.LikeRequest;

@Service
public class LikeMapper {

    public LikeReponse toLikeReponse(Like like) {
        return LikeReponse.builder()
                .id(like.getId())
                .createdTime(like.getCreatedTime())
                .build();
    }

    public Like toLike(LikeRequest likeRequest) {
        return Like.builder().createdTime(LocalDateTime.now()).build();
    }
}
