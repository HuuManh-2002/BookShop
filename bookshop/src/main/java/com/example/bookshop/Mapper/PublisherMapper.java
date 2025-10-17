package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Publisher;
import com.example.bookshop.EntityDto.Reponse.PublisherReponse;
import com.example.bookshop.EntityDto.Request.PublisherRequest;

@Service
public class PublisherMapper {

    public Publisher toPublisher(PublisherRequest publisherRequest) {
        return Publisher.builder()
                .name(publisherRequest.getName())
                .description(publisherRequest.getDescription())
                .createTime(LocalDateTime.now())
                .build();
    }

    public PublisherReponse toPublisherReponse(Publisher publisher) {
        return PublisherReponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .description(publisher.getDescription())
                .createTime(publisher.getCreateTime())
                .build();
    }
}
