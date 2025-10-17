package com.example.bookshop.EntityDto.Reponse;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressReponse {
    Long id;
    String recipientName;
    String phoneNumberRecipient;
    int houseNumber;
    String street;
    String commune;
    String ward;
    String district;
    String city;
    String description;
    LocalDateTime createTime;

}
