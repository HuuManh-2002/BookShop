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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.UserReponse;
import com.example.bookshop.EntityDto.Request.UserRequest;
import com.example.bookshop.EntityDto.Update.UserUpdate;
import com.example.bookshop.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public ApiReponse<List<UserReponse>> getAll() {

        return ApiReponse.<List<UserReponse>>builder()
                .code(1000)
                .result(userService.getAll())
                .build();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ApiReponse<UserReponse> create(
            @RequestPart("userRequest") @Valid UserRequest userRequest,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        return ApiReponse.<UserReponse>builder()
                .code(1000)
                .result(userService.create(userRequest, avatarFile))
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<UserReponse> get(@PathVariable Long id) {
        return ApiReponse.<UserReponse>builder()
                .code(1000)
                .result(userService.get(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<UserReponse> update(@RequestBody UserUpdate userUpdate, @PathVariable Long id) {

        return ApiReponse.<UserReponse>builder()
                .code(1000)
                .result(userService.update(userUpdate, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

    @GetMapping("/me")
    public ApiReponse<UserReponse> getMyInfor() {
        return ApiReponse.<UserReponse>builder()
                .code(1000)
                .result(userService.getMyInfor())
                .build();
    }

    @PutMapping(value = "/me", consumes = "multipart/form-data")
    public ApiReponse<UserReponse> updateMyInfor(@RequestPart("userUpdate") @Valid UserUpdate userUpdate,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {
        return ApiReponse.<UserReponse>builder()
                .code(1000)
                .result(userService.updateMyInfor(userUpdate, avatarFile))
                .build();
    }

    @DeleteMapping("/me")
    public ApiReponse<String> deleteMyInfor() {
        userService.deleteMyInfor();
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }

}
