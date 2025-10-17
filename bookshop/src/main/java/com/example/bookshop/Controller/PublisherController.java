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
import com.example.bookshop.EntityDto.Reponse.PublisherReponse;
import com.example.bookshop.EntityDto.Request.PublisherRequest;
import com.example.bookshop.EntityDto.Update.PublisherUpdate;
import com.example.bookshop.Service.PublisherService;
@RestController
@RequestMapping("/publisher")
public class PublisherController {
 @Autowired
    PublisherService publisherService;

    @GetMapping()
    public ApiReponse<List<PublisherReponse>> getAll() {
        return ApiReponse.<List<PublisherReponse>>builder()
                .code(1000)
                .result(publisherService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiReponse<PublisherReponse> getById(@PathVariable Long id) {
        return ApiReponse.<PublisherReponse>builder()
                .code(1000)
                .result(publisherService.get(id))
                .build();
    }

    @PostMapping()
    public ApiReponse<PublisherReponse> create(@RequestBody PublisherRequest publisherRequest) {
        return ApiReponse.<PublisherReponse>builder()
                .code(1000)
                .result(publisherService.create(publisherRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<PublisherReponse> update(@PathVariable Long id, @RequestBody PublisherUpdate publisherUpdate) {

        return ApiReponse.<PublisherReponse>builder()
                .code(1000)
                .result(publisherService.update(publisherUpdate, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiReponse<String> delete(@PathVariable Long id) {

        publisherService.delete(id);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Deleted")
                .build();
    }
}
