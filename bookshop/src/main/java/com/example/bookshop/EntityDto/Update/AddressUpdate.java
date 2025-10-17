package com.example.bookshop.EntityDto.Update;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressUpdate {

    String recipientName;
    String phoneNumberRecipient;
    int houseNumber;
    String street;
    String commune;
    String ward;
    String district;
    String city;
    String description;
}
