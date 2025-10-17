package com.example.bookshop.EntityDto.Reponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReponse {
    Long id;
    String firstName;
    String lastName;
    String username;
    String password;
    String email;
    String phoneNumber;
    LocalDate dob;
    String gender;
    LocalDateTime createdTime;

}
