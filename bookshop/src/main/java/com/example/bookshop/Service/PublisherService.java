package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Publisher;
import com.example.bookshop.EntityDto.Reponse.PublisherReponse;
import com.example.bookshop.EntityDto.Request.PublisherRequest;
import com.example.bookshop.EntityDto.Update.PublisherUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.PublisherMapper;
import com.example.bookshop.Repository.PublisherRepository;

@Service
public class PublisherService {
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    PublisherMapper publisherMapper;

    public List<PublisherReponse> getAll() {
        List<Publisher> publishers = publisherRepository.findAll();
        List<PublisherReponse> publisherReponses = new ArrayList<>();
        for (Publisher publisher : publishers) {
            publisherReponses.add(publisherMapper.toPublisherReponse(publisher));
        }
        return publisherReponses;
    }

    public PublisherReponse get(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
        return publisherMapper.toPublisherReponse(publisher);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public PublisherReponse create(PublisherRequest publisherRequest) {
        Publisher publisher = publisherMapper.toPublisher(publisherRequest);
        publisherRepository.save(publisher);
        return publisherMapper.toPublisherReponse(publisher);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public PublisherReponse update(PublisherUpdate publisherUpdate, Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
        publisher.setName(publisherUpdate.getName());
        publisher.setDescription(publisherUpdate.getDescription());

        publisherRepository.save(publisher);
        return publisherMapper.toPublisherReponse(publisher);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new AppException(ErrorCode.PUBLISHER_NOT_FOUND);
        }
        publisherRepository.deleteById(id);
    }
}
