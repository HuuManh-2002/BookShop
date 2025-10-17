package com.example.bookshop.Mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Address;
import com.example.bookshop.EntityDto.Reponse.AddressReponse;
import com.example.bookshop.EntityDto.Request.AddressRequest;

@Service
public class AddressMapper {

    public AddressReponse toAddressReponse(Address address) {
        return AddressReponse.builder()
                .id(address.getId())
                .recipientName(address.getRecipientName())
                .phoneNumberRecipient(address.getPhoneNumberRecipient())
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .commune(address.getCommune())
                .ward(address.getWard())
                .district(address.getDistrict())
                .city(address.getCity())
                .description(address.getDescription())
                .createTime(address.getCreateTime())
                .build();
    }

    public Address toAddress(AddressRequest addressRequest) {
        return Address.builder()
                .recipientName(addressRequest.getRecipientName())
                .phoneNumberRecipient(addressRequest.getPhoneNumberRecipient())
                .houseNumber(addressRequest.getHouseNumber())
                .street(addressRequest.getStreet())
                .commune(addressRequest.getCommune())
                .ward(addressRequest.getWard())
                .district(addressRequest.getDistrict())
                .city(addressRequest.getCity())
                .description(addressRequest.getDescription())
                .createTime(LocalDateTime.now())
                .build();
    }
}
