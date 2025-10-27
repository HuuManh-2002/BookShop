package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.UserReponse;
import com.example.bookshop.EntityDto.Request.UserRequest;

@Service
public class UserMapper {

    public User toUser(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .dob(userRequest.getDob())
                .gender(userRequest.getGender())
                .created_Time(LocalDateTime.now())
                .build();
    }

    public UserReponse toUserReponse(User user) {
        return UserReponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .savedFileName(user.getSavedFileName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .gender(user.getGender())
                .createdTime(user.getCreated_Time())
                .build();
    }
}
