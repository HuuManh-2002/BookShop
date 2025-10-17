package com.example.bookshop.Mapper;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Status;
import com.example.bookshop.EntityDto.Reponse.StatusReponse;
import com.example.bookshop.EntityDto.Request.StatusRequest;

@Service
public class StatusMapper {

    public StatusReponse toStatusReponse(Status status) {
        return StatusReponse.builder()
                .id(status.getId())
                .name(status.getName())
                .description(status.getDescription())
                .build();
    }

    public Status toStatus(StatusRequest statusRequest) {
        return Status.builder()
                .name(statusRequest.getName())
                .description(statusRequest.getDescription())
                .build();
    }

}
