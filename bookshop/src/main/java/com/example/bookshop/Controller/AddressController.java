package com.example.bookshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.AddressReponse;
import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Request.AddressRequest;
import com.example.bookshop.EntityDto.Update.AddressUpdate;
import com.example.bookshop.Service.AddressService;

@RequestMapping("/address")
@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping()
    public ApiReponse<List<AddressReponse>> getAll() {
        return ApiReponse.<List<AddressReponse>>builder()
                .code(1000)
                .result(addressService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<AddressReponse> get(@PathVariable Long id) {
        return ApiReponse.<AddressReponse>builder()
                .code(1000)
                .result(addressService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<AddressReponse> create(@RequestBody AddressRequest addressRequest) {
        return ApiReponse.<AddressReponse>builder()
                .code(1000)
                .result(addressService.create(addressRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<AddressReponse> update(@PathVariable Long id, @RequestBody AddressUpdate addressUpdate) {
        return ApiReponse.<AddressReponse>builder()
                .code(1000)
                .result(addressService.update(id, addressUpdate))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
