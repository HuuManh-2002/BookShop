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

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.StatusReponse;
import com.example.bookshop.EntityDto.Request.StatusRequest;
import com.example.bookshop.EntityDto.Update.StatusUpdate;
import com.example.bookshop.Service.StatusService;

@RequestMapping("/status")
@RestController
public class StatusController {
    @Autowired
    StatusService statusService;

    @GetMapping()
    public ApiReponse<List<StatusReponse>> getAll() {
        return ApiReponse.<List<StatusReponse>>builder()
                .code(1000)
                .result(statusService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<StatusReponse> get(@PathVariable Long id) {
        return ApiReponse.<StatusReponse>builder()
                .code(1000)
                .result(statusService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<StatusReponse> create(@RequestBody StatusRequest statusRequest) {
        return ApiReponse.<StatusReponse>builder()
                .code(1000)
                .result(statusService.create(statusRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<StatusReponse> update(@PathVariable Long id, @RequestBody StatusUpdate statusUpdate) {

        return ApiReponse.<StatusReponse>builder()
                .code(1000)
                .result(statusService.update(id, statusUpdate))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        statusService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}
