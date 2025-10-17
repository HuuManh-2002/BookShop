package com.example.bookshop.Controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.EntityDto.Reponse.AuthenticationReponse;
import com.example.bookshop.EntityDto.Reponse.IntrospectResponse;
import com.example.bookshop.EntityDto.Request.AuthenticationRequest;
import com.example.bookshop.EntityDto.Request.IntrospectRequest;
import com.example.bookshop.EntityDto.Request.LogoutRequest;
import com.example.bookshop.EntityDto.Request.RefreshRequest;
import com.example.bookshop.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiReponse<AuthenticationReponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

        return ApiReponse.<AuthenticationReponse>builder()
                .code(1000)
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/introspect")
    public ApiReponse<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {
        return ApiReponse.<IntrospectResponse>builder()
                .code(1000)
                .result(authenticationService.introspectToken(introspectRequest))
                .build();

    }

    @PostMapping("/logout")
    public ApiReponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws JOSEException, ParseException {
        authenticationService.logout(logoutRequest);
        return ApiReponse.<String>builder()
                .code(1000)
                .result("Logouted")
                .build();
    }

    @PostMapping("/refresh")
    public ApiReponse<AuthenticationReponse> refreshToken(@RequestBody RefreshRequest refreshRequest)
            throws ParseException, JOSEException {
        return ApiReponse.<AuthenticationReponse>builder()
                .code(1000)
                .result(authenticationService.refreshToken(refreshRequest))
                .build();
    }
}
