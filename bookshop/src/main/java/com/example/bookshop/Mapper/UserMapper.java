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
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .dob(userRequest.getDob())
                .email(userRequest.getEmail())
                .gender(userRequest.getGender())
                .created_Time(LocalDateTime.now())
                .build();
    }

    public UserReponse toUserReponse(User user) {
        return UserReponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .email(user.getEmail())
                .gender(user.getGender())
                .createdTime(user.getCreated_Time())
                .build();
    }
}
