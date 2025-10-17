package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Address;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.AddressReponse;
import com.example.bookshop.EntityDto.Request.AddressRequest;
import com.example.bookshop.EntityDto.Update.AddressUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.AddressMapper;
import com.example.bookshop.Repository.AddressRepository;
import com.example.bookshop.Repository.UserRepository;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    UserService userService;

    public List<AddressReponse> getAll() {
        User user = userService.getUserformToKen();
        List<Address> addresses = addressRepository.findByUser(user);
        List<AddressReponse> addressReponses = new ArrayList<>();
        for (Address address : addresses) {
            addressReponses.add(addressMapper.toAddressReponse(address));
        }
        return addressReponses;
    }

    public AddressReponse create(AddressRequest addressRequest){
        User user = userService.getUserformToKen();
        Address address = addressMapper.toAddress(addressRequest);
        address.setUser(user);
        addressRepository.save(address);
        return addressMapper.toAddressReponse(address);
    }

    public AddressReponse get(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        User user = userService.getUserformToKen();
        if (!user.getId().equals(address.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return addressMapper.toAddressReponse(address);
    }

    public AddressReponse update(Long id, AddressUpdate addressUpdate) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        User user = userService.getUserformToKen();
        if (!user.getId().equals(address.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        address.setRecipientName(addressUpdate.getRecipientName());
        address.setPhoneNumberRecipient(addressUpdate.getPhoneNumberRecipient());
        address.setHouseNumber(addressUpdate.getHouseNumber());
        address.setStreet(addressUpdate.getStreet());
        address.setCommune(addressUpdate.getCommune());
        address.setWard(addressUpdate.getWard());
        address.setCity(addressUpdate.getCity());
        address.setDescription(addressUpdate.getDescription());

        addressRepository.save(address);
        return addressMapper.toAddressReponse(address);
    }

    public void delete(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        User user = userService.getUserformToKen();
        if (!user.getId().equals(address.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        addressRepository.delete(address);
    }

}
