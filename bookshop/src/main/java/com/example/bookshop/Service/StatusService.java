package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Status;
import com.example.bookshop.EntityDto.Reponse.StatusReponse;
import com.example.bookshop.EntityDto.Request.StatusRequest;
import com.example.bookshop.EntityDto.Update.StatusUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.StatusMapper;
import com.example.bookshop.Repository.StatusRepository;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;
    @Autowired
    StatusMapper statusMapper;

    public List<StatusReponse> getAll() {
        List<Status> statuses = statusRepository.findAll();
        List<StatusReponse> statusReponses = new ArrayList<>();
        for (Status status : statuses) {
            statusReponses.add(statusMapper.toStatusReponse(status));

        }
        return statusReponses;
    }

    public StatusReponse get(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
        return statusMapper.toStatusReponse(status);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public StatusReponse create(StatusRequest statusRequest) {
        Status status = statusMapper.toStatus(statusRequest);
        statusRepository.save(status);
        return statusMapper.toStatusReponse(status);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public StatusReponse update(Long id, StatusUpdate statusUpdate) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));

        status.setName(statusUpdate.getName());
        status.setDescription(statusUpdate.getDescription());

        statusRepository.save(status);
        return statusMapper.toStatusReponse(status);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
        statusRepository.delete(status);
    }
}
