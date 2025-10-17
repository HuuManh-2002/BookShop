package com.example.bookshop.EntityDto.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRequest {

    String firstName;
    String lastName;
    @Size(min = 5, message = "USERNAME_SHORT")
    String username;
    @Size(min = 8, message = "PASSWORD_SHORT")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "PASSWORD_MISSING_ALPHANUMERIC")
    String password;
    String email;
    String phoneNumber;
    LocalDate dob;
    String gender;
}
